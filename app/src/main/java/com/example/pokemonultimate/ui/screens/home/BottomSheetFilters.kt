package com.example.pokemonultimate.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.ui.utils.Padding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayBottomSheetFilter(
    onDismissBottomSheet: () -> Unit,
    homeViewModel: HomeViewModel,
    onConfirmFiltersTypes: ((List<String>) -> Unit),
    onConfirmFiltersSuperTypes: ((List<String>) -> Unit),
    onConfirmFiltersSubTypes: ((List<String>) -> Unit),
    filters: List<String>,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismissBottomSheet.invoke()
        },
        sheetState = sheetState,
    ) {
        var filterScreenToDisplay: FilterCategories? by remember {
            mutableStateOf(null)
        }

        Column(Modifier.padding(bottom = Padding.BIG.dp)) {

            when (filterScreenToDisplay) {
                FilterCategories.SUBTYPES -> {
                    ListCheckableCategories(
                        homeViewModel,
                        sheetState,
                        FilterCategories.SUBTYPES,
                        onConfirmFilters = {
                            onConfirmFiltersSubTypes.invoke(it)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        currentFilters = filters,
                    )
                }

                FilterCategories.TYPES -> {
                    ListCheckableCategories(
                        homeViewModel, sheetState, FilterCategories.TYPES,
                        onConfirmFilters = {
                            onConfirmFiltersTypes.invoke(it)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        currentFilters = filters,
                    )
                }

                FilterCategories.SUPERTYPE -> {
                    ListCheckableCategories(
                        homeViewModel, sheetState, FilterCategories.SUPERTYPE,
                        onConfirmFilters = {
                            onConfirmFiltersSuperTypes.invoke(it)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        currentFilters = filters,
                    )
                }

                null -> {
                    FilterCategories.entries.forEachIndexed { index, filterCategories ->
                        if (index == 0) CategoryDivider()
                        Text(
                            filterCategories.nameUi,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Padding.NORMAL.dp)
                                .clickable {
                                    filterScreenToDisplay = filterCategories
                                },
                        )
                        CategoryDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonFilters(
    numberOfFilters: Int,
    onButtonClicked: () -> Unit
) {
    val buttonColorContainer = if (isSystemInDarkTheme()) Color(0XFF393B40) else Color(0XFF006874)
    val buttonColorFont = Color(0XFFFFFFFF)

    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        enabled = numberOfFilters > 0,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.BIG.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = buttonColorFont,
            containerColor = buttonColorContainer,
            disabledContentColor = buttonColorFont,
            disabledContainerColor = buttonColorContainer,
        )
    ) {
        if (numberOfFilters > 0) {
            Text("Confirm filters $numberOfFilters")
        } else {
            Text("No filters selected")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCheckableCategories(
    homeViewModel: HomeViewModel,
    sheetState: SheetState,
    filterCategories: FilterCategories,
    onConfirmFilters: ((List<String>) -> Unit),
    currentFilters: List<String>,
) {
    val mutableList = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            sheetState.expand()
            homeViewModel.getFilters(filterCategories)
        }
    }

    Column {
        var text by remember { mutableStateOf("") }
        var expanded by rememberSaveable { mutableStateOf(false) }
        val filtersListFromApi by homeViewModel.getFilteredFilters(text).collectAsState()

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Padding.NORMAL.dp, start = Padding.BIG.dp, end = Padding.BIG.dp),
            windowInsets = WindowInsets(top = 0.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = {
                        text = it
                    },
                    onSearch = { },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search filters") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "filter icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    text = ""
                                }
                        )
                    },
                )
            },
            colors = SearchBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                dividerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            expanded = false,
            onExpandedChange = { expanded = it },
        ) {}
        Box(contentAlignment = Alignment.BottomCenter) {
            LazyColumn(modifier = Modifier.padding(bottom = Padding.ULTRA_HUGE.dp)) {
                items(filtersListFromApi) { filterName ->
                    if (currentFilters.contains(filterName)) {
                        mutableList.add(filterName)
                    }
                    ItemCheckable(filterName, mutableList)
                }
            }
            ButtonFilters(mutableList.size, onButtonClicked = {
                val list = mutableList.mapNotNull {
                    if (filtersListFromApi.contains(it)) it else null
                }
                onConfirmFilters.invoke(list)
            })
        }
    }
}

@Composable
fun ItemCheckable(
    filterName: String,
    mutableList: SnapshotStateList<String>,
) {
    var checkedState: Boolean by remember { mutableStateOf(mutableList.contains(filterName)) }
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = {
                    checkedState = it
                    if (checkedState) {
                        mutableList.add(filterName)
                    } else {
                        mutableList.remove(filterName)
                    }
                },
                role = Role.Checkbox,
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null,
        )
        Text(
            text = filterName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun CategoryDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = Padding.HUGE.dp)
    )
}
