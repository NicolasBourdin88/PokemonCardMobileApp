package com.example.pokemonultimate.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pokemonultimate.ui.utils.Padding

@Composable
fun DisplayScannedCardsScreen(imageUrls: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(imageUrls) { url ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Padding.MEDIUM.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Carte Pokémon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
