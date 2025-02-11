package com.example.pokemonultimate.ui.screens.collection.cardList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.ui.navigation.CollectionNavigation
import com.example.pokemonultimate.ui.screens.collection.CollectionViewModel
import com.example.pokemonultimate.ui.theme.cardLightingColorFilter
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun CardListScreen(
    setId: String,
    setImage: String,
    cardFromHome: String? = null,
    collectionViewModel: CollectionViewModel,
    navController: NavHostController
) {
    Column {
        Header(setImage)
        ListCardSearchResult(
            setId = setId,
            cardFromHome = cardFromHome,
            collectionViewModel = collectionViewModel,
            navController = navController
        )
    }
}

@Composable
private fun Header(setImage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        AsyncImage(
            setImage,
            contentDescription = "Set image",
            modifier = Modifier
                .padding(Padding.MINI.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = 0.1F,
        )
        TitleText("Your Collection")
    }
}

@Composable
fun ListCardSearchResult(
    collectionViewModel: CollectionViewModel,
    setId: String,
    cardFromHome: String? = null,
    navController: NavHostController,
) {
    val pager = collectionViewModel.getFlow(setId = setId)
    val lazyPagingItems = pager.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(horizontal = Padding.MINI.dp),
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
                val isHighlighted = it.id == cardFromHome
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(Padding.MINI.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(
                                brush = if (isHighlighted) Brush.radialGradient(
                                    colors = listOf(
                                        cardLightingColorFilter.copy(alpha = 0.60f),
                                        cardLightingColorFilter.copy(alpha = 0.85f),
                                        cardLightingColorFilter.copy(alpha = 0.60f),
                                        Color.Transparent
                                    ),
                                    center = androidx.compose.ui.geometry.Offset.Unspecified,
                                    radius = 500f
                                ) else Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            )
                    )
                    AsyncImage(
                        model = it.images.large,
                        contentDescription = "Card",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .clickable {
                                val jsonCard = Json.Default.encodeToString<PokemonCardEntity>(it)
                                navController.navigate(
                                    CollectionNavigation.CardDestination(jsonCard, false)
                                )
                            }
                    )
                }
            }
        }


    }
}
