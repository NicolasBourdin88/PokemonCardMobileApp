package com.example.pokemonultimate.ui.screens.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.theme.cardFireFirstColor
import com.example.pokemonultimate.ui.theme.cardFireSecondColor
import com.example.pokemonultimate.ui.theme.cardGrassFirstColor
import com.example.pokemonultimate.ui.theme.cardGrassSecondColor
import com.example.pokemonultimate.ui.theme.cardLightningFirstColor
import com.example.pokemonultimate.ui.theme.cardLightningSecondColor
import com.example.pokemonultimate.ui.theme.cardWaterFirstColor
import com.example.pokemonultimate.ui.theme.cardWaterSecondColor
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir
import com.example.pokemonultimate.ui.utils.setFirstToUpperCase

private const val DEFAULT_WITH_POKEMON_CELL = 200
private const val DEFAULT_PADDING_BOTTOM_POKEMON_CELL = 40


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
) {
    var querySearch: String? by remember { mutableStateOf(null) }
    var filtersTypes = remember { mutableStateListOf<String>() }
    var filtersSubTypes = remember { mutableStateListOf<String>() }
    var filtersSuperTypes = remember { mutableStateListOf<String>() }
    var displayBottomSheet: Boolean by remember { mutableStateOf(false) }
    var notInSearchMode: Boolean by remember { mutableStateOf(true) }

    notInSearchMode = hasNoFilters(filtersTypes, filtersSubTypes, filtersSuperTypes)
            && querySearch.isNullOrEmpty() == true

    Column {
        if (notInSearchMode) TitleText("What Are You Looking For")
        HomeSearchBar(onSearch = { query ->
            querySearch = query
        }, onFilterClicked = {
            displayBottomSheet = true
        })
        if (notInSearchMode) {
            ListCardPokemonType(onCardClick = { cardType ->
                filtersTypes.add(cardType.setFirstToUpperCase())
            })
        } else {
            ListFilters(
                filtersTypes = filtersTypes,
                filtersSubTypes = filtersSubTypes,
                filtersSuperTypes = filtersSuperTypes,
                onRemoveFilter = { filter ->
                    if (filtersTypes.contains(filter)) {
                        filtersTypes.remove(filter)
                    } else if (filtersSubTypes.contains(filter)) {
                        filtersSubTypes.remove(filter)
                    } else if (filtersSuperTypes.contains(filter)) {
                        filtersSuperTypes.remove(filter)
                    }
                }
            )
            ListCardSearchResult(
                homeViewModel, query = querySearch,
                filtersTypes = filtersTypes,
                filtersSubTypes = filtersSubTypes,
                filtersSuperTypes = filtersSuperTypes,
            )
        }
    }

    if (displayBottomSheet) {
        DisplayBottomSheetFilter(
            filters = filtersTypes + filtersSubTypes + filtersSuperTypes,
            homeViewModel = homeViewModel,
            onDismissBottomSheet = {
                displayBottomSheet = false
            },
            onConfirmFiltersTypes = { listFilters ->
                filtersTypes.replaceAllValue(listFilters)
                displayBottomSheet = false
            }, onConfirmFiltersSubTypes = { listFilters ->
                filtersSubTypes.replaceAllValue(listFilters)
                displayBottomSheet = false
            }, onConfirmFiltersSuperTypes = { listFilters ->
                filtersSuperTypes.replaceAllValue(listFilters)
                displayBottomSheet = false
            }
        )
    }
}

@Composable
fun ListCardSearchResult(
    homeViewModel: HomeViewModel,
    query: String?,
    filtersTypes: List<String>,
    filtersSubTypes: List<String>,
    filtersSuperTypes: List<String>
) {
    val immutableFiltersTypes = filtersTypes.toList()
    val immutableFiltersSubTypes = filtersSubTypes.toList()
    val immutableFiltersSuperTypes = filtersSuperTypes.toList()

    val pager = homeViewModel.getFlow(
        query,
        filtersTypes = immutableFiltersTypes,
        filtersSubTypes = immutableFiltersSubTypes,
        filtersSuperTypes = immutableFiltersSuperTypes,
    )
    val lazyPagingItems = pager.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(horizontal = Padding.NORMAL.dp),
    ) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                item {
                    Text("Loading...")
                }
            }

            is LoadState.Error -> {
                val error = lazyPagingItems.loadState.refresh as LoadState.Error
                item {
                    Text("Error: ${error.error.localizedMessage}")
                }
            }

            is LoadState.NotLoading -> {
                if (lazyPagingItems.itemCount == 0) {
                    item {
                        Text("No results found.")
                    }
                }
            }
        }

        items(lazyPagingItems.itemCount) { index ->
            val pokemonCellInfo = lazyPagingItems[index]
            pokemonCellInfo?.let {
                AsyncImage(
                    it.images.large,
                    contentDescription = "Card",
                    modifier = Modifier
                        .width(100.dp)
                        .padding(Padding.NORMAL.dp),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}

@Composable
private fun PokemonCard(pokemonCellInfo: PokemonCellInfo, onCardClick: (String) -> Unit) {
    Box(modifier = Modifier.clickable {
        onCardClick.invoke(pokemonCellInfo.name)
    }) {
        CardBackground(pokemonCellInfo)
        ImagePokemon(pokemonCellInfo)
    }
}

@Composable
private fun ImagePokemon(pokemonCellInfo: PokemonCellInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(pokemonCellInfo.pokemonCellImage),
            contentDescription = pokemonCellInfo.name,
            modifier = pokemonCellInfo.modifier,
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun CardBackground(pokemonCellInfo: PokemonCellInfo) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(Padding.NORMAL.dp)
            .height(200.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = pokemonCellInfo.brush)
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                pokemonCellInfo.name.setFirstToUpperCase(),
                modifier = Modifier.weight(1F),
                color = Color.White,
                fontFamily = fontFamilyAvenir,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
            )
            Icon(
                painter = painterResource(R.drawable.arrow_right_card),
                contentDescription = "Direction arrow",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ListCardPokemonType(onCardClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(horizontal = Padding.NORMAL.dp),
    ) {
        items(PokemonCellInfo.entries) { pokemonCellInfo ->
            PokemonCard(pokemonCellInfo, onCardClick = {
                onCardClick.invoke(it)
            })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeSearchBar(onSearch: (query: String) -> Unit, onFilterClicked: () -> Unit) {
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
                    Icon(painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "filter icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onFilterClicked.invoke()
                            })
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

private fun hasNoFilters(
    filtersTypes: SnapshotStateList<String>,
    filtersSubTypes: SnapshotStateList<String>,
    filtersSuperTypes: SnapshotStateList<String>
): Boolean = filtersTypes.isEmpty() && filtersSubTypes.isEmpty() && filtersSuperTypes.isEmpty()

private fun <T> MutableList<T>.replaceAllValue(newList: List<T>) {
    clear()
    addAll(newList)
}

private enum class PokemonCellInfo(
    @DrawableRes val pokemonCellImage: Int,
    val brush: Brush,
    val modifier: Modifier,
) {
    FIRE(
        pokemonCellImage = R.drawable.image_card_fire,
        brush = Brush.linearGradient(listOf(cardFireFirstColor, cardFireSecondColor)),
        modifier = Modifier
            .padding(bottom = DEFAULT_PADDING_BOTTOM_POKEMON_CELL.dp, end = 20.dp)
            .width(DEFAULT_WITH_POKEMON_CELL.dp),
    ),
    GRASS(
        pokemonCellImage = R.drawable.image_card_plant,
        brush = Brush.linearGradient(listOf(cardGrassFirstColor, cardGrassSecondColor)),
        modifier = Modifier
            .padding(bottom = DEFAULT_PADDING_BOTTOM_POKEMON_CELL.dp)
            .width(DEFAULT_WITH_POKEMON_CELL.dp),
    ),
    ELECTRIC(
        pokemonCellImage = R.drawable.image_card_electric,
        brush = Brush.linearGradient(listOf(cardLightningFirstColor, cardLightningSecondColor)),
        modifier = Modifier
            .padding(bottom = DEFAULT_PADDING_BOTTOM_POKEMON_CELL.dp)
            .width(DEFAULT_WITH_POKEMON_CELL.dp),
    ),
    WATER(
        pokemonCellImage = R.drawable.image_card_water,
        brush = Brush.linearGradient(listOf(cardWaterFirstColor, cardWaterSecondColor)),
        modifier = Modifier
            .padding(bottom = 40.dp)
            .width(125.dp),
    ),
}

enum class FilterCategories(val nameUi: String) {
    SUBTYPES("SubTypes"), TYPES("Types"), SUPERTYPE("SuperType")
}
