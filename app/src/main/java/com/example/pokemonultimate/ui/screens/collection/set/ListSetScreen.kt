package com.example.pokemonultimate.ui.screens.collection.set

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.pokemonultimate.data.model.sets.Set
import com.example.pokemonultimate.ui.navigation.CollectionNavigation
import com.example.pokemonultimate.ui.navigation.navigateToSelectedItem
import com.example.pokemonultimate.ui.screens.collection.CollectionViewModel
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir

@Composable
fun ListSetScreen(navController: NavHostController) {
    Column {
        TitleText("Your Collection")
        ListCollections(navController = navController)
        SearchBarCollection(
            onSearch = {

            }
        )
    }
}

@Composable
fun ListCollections(
    homeViewModel: CollectionViewModel = viewModel(),
    navController: NavHostController
) {
    val sets by homeViewModel.setsFlow.collectAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(horizontal = Padding.NORMAL.dp),
    ) {
        items(sets) { set ->
            ItemSet(set, navController)
        }
    }
}

@Composable
fun ItemSet(set: Set, navController: NavHostController) {
    Column(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(horizontal = Padding.NORMAL.dp)
            .clickable {
                navController.navigateToSelectedItem(CollectionNavigation.CardListDestination)
            }
    ) {
        AsyncImage(
            set.images.logo,
            contentDescription = "Set image",
            modifier = Modifier
                .padding(Padding.NORMAL.dp)
                .height(70.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit,
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "0/${set.total}",
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = fontFamilyAvenir,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = Padding.NORMAL.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCollection(onSearch: (query: String) -> Unit) {
    var text by remember {
        mutableStateOf("")
    }
    var expanded by rememberSaveable { mutableStateOf(false) }

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
                    onSearch.invoke(it)
                },
                onSearch = { onSearch.invoke(it) },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search card") },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Clear,
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
}
