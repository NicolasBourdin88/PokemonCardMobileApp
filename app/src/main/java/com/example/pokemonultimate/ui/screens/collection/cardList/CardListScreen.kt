package com.example.pokemonultimate.ui.screens.collection.cardList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.utils.getUserId
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
        val isNetworkAvailable = collectionViewModel.isNetworkAvailable.collectAsState()

        if (isNetworkAvailable.value == true) {
            ListCardSearchResult(setId, collectionViewModel, cardFromHome, navController)
        } else {
            ListCardNoConnection(setId, collectionViewModel, cardFromHome, navController)
        }
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
fun ListCardNoConnection(setId: String, collectionViewModel: CollectionViewModel, cardFromHome: String?, navController: NavHostController) {

    val offlineCards = collectionViewModel.getOfflineCardsFromSet(setId)
    if (offlineCards.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.padding(horizontal = Padding.MINI.dp),
        ) {
            items(offlineCards) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.images.large)
                        .placeholder(R.drawable.back_card)
                        .error(R.drawable.back_card)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Card",
                    modifier = Modifier
                        .width(100.dp)
                        .padding(Padding.MINI.dp),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    } else {
        if (getUserId() == null) {
            Text("You need to be connected to see offline cards from your collection")
        } else {
            Text("No cards from this set in the collection")
        }
    }
}

@Composable
fun ListCardSearchResult(setId: String, collectionViewModel: CollectionViewModel, cardFromHome: String?, navController: NavHostController) {

    val pager = collectionViewModel.getFlow(
        setId = setId,
    )
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
                val context = LocalContext.current
                val isHighlighted = it.id == cardFromHome
                val alpha = if (collectionViewModel.userHaveCards(it)) 1f else 0.4f

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(Padding.MINI.dp)
                ) {
                    // Effet de surbrillance conditionnel
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(8.dp))
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
                    // Image de la carte avec opacité et clic
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(it.images.large)
                            .placeholder(R.drawable.back_card)
                            .error(R.drawable.back_card)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Card",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .clip(RoundedCornerShape(8.dp))
                            .alpha(alpha)
                            .clickable {
                                val jsonCard = Json.Default.encodeToString<PokemonCardEntity>(it)
                                navController.navigate(CollectionNavigation.CardDestination(jsonCard, false))
                            }
                    )
                }
            }
        }

    }
}
