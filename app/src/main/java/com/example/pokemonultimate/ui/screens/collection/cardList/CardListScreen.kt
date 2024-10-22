package com.example.pokemonultimate.ui.screens.collection.cardList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.pokemonultimate.ui.screens.collection.CollectionViewModel
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText

@Composable
fun CardListScreen(setId: String, setImage: String, collectionViewModel: CollectionViewModel) {
    Column {
        Header(setImage)
        ListCardSearchResult(setId = setId, collectionViewModel = collectionViewModel)
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
) {
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
                AsyncImage(
                    it.images.large,
                    contentDescription = "Card",
                    modifier = Modifier
                        .width(100.dp)
                        .padding(Padding.MINI.dp),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}
