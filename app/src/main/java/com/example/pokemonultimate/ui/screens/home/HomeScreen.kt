package com.example.pokemonultimate.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.pokemonultimate.ui.theme.cardElectricFirstColor
import com.example.pokemonultimate.ui.theme.cardElectricSecondColor
import com.example.pokemonultimate.ui.theme.cardFireFirstColor
import com.example.pokemonultimate.ui.theme.cardFireSecondColor
import com.example.pokemonultimate.ui.theme.cardPlantFirstColor
import com.example.pokemonultimate.ui.theme.cardPlantSecondColor
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
    var notInSearchMode: Boolean by remember {
        mutableStateOf(true)
    }
    var querySearch: String? by remember {
        mutableStateOf(null)
    }
    var filters: String? by remember {
        mutableStateOf(null)
    }

    Column {
        if (notInSearchMode) TitleText("What Are You Looking For")
        HomeSearchBar(
            onSearch = { query ->
                notInSearchMode = query.isEmpty()
                if (!notInSearchMode) querySearch = query
            }
        )
        if (notInSearchMode) {
            ListCardPokemonType(
                onCardClick = { cardType ->
                    filters = cardType
                    notInSearchMode = false
                }
            )
        } else {
            ListCardSearchResult(homeViewModel, query = querySearch, filters = filters)
        }
    }
}

@Composable
fun ListCardSearchResult(
    homeViewModel: HomeViewModel,
    query: String?,
    filters: String?,
) {
    val pager = homeViewModel.getFlow(query, filters)
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
    Box(
        modifier = Modifier.clickable {
            onCardClick.invoke(pokemonCellInfo.name)
        }
    ) {
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
private fun HomeSearchBar(onSearch: (query: String) -> Unit) {
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
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
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
        brush = Brush.linearGradient(listOf(cardPlantFirstColor, cardPlantSecondColor)),
        modifier = Modifier
            .padding(bottom = DEFAULT_PADDING_BOTTOM_POKEMON_CELL.dp)
            .width(DEFAULT_WITH_POKEMON_CELL.dp),
    ),
    ELECTRIC(
        pokemonCellImage = R.drawable.image_card_electric,
        brush = Brush.linearGradient(listOf(cardElectricFirstColor, cardElectricSecondColor)),
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
