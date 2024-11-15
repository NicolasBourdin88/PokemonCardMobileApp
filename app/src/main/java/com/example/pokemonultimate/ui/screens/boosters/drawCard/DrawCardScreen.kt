package com.example.pokemonultimate.ui.screens.boosters.drawCard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DrawCardScreen(setId: String, drawCardViewModel: DrawCardViewModel = viewModel()) {
    Text(drawCardViewModel.cardToDisplay.firstOrNull()?.name ?: setId)
    LaunchedEffect(setId) {
        drawCardViewModel.getCardsToDraw(setId)
    }
}
