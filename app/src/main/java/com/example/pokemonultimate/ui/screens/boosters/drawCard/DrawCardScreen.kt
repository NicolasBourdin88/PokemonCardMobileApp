package com.example.pokemonultimate.ui.screens.boosters.drawCard

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.ui.utils.Padding
import kotlinx.coroutines.delay

@Composable
fun DrawCardScreen(setId: String, drawCardViewModel: DrawCardViewModel = viewModel()) {
    LaunchedEffect(setId) { drawCardViewModel.getCardsToDraw(setId) }

    val cards by drawCardViewModel.cardToDisplay.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DisplayBoostersContent(cards)
    }
}

@Composable
fun DisplayBoostersContent(cards: List<PokemonCardEntity>) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(cards.firstOrNull()) {
        cards.firstOrNull()?.images?.large?.let { imageUrl ->
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()
            val result = imageLoader.execute(request)
            bitmap = (result.drawable as? BitmapDrawable)?.bitmap
        }
    }

    if (cards.isNotEmpty() && bitmap != null) {
        var cardFace by remember { mutableStateOf(CardFace.Back) }

        LaunchedEffect(cardFace) {
            delay(500)
            cardFace = CardFace.Front
        }

        FlipCard(
            cardFace = cardFace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.GIANT.dp),
            front = {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            }, back = {
                Image(
                    painter = painterResource(R.drawable.back_card),
                    contentDescription = "Dos de carte",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            }
        )
    } else {
        Image(
            painter = painterResource(R.drawable.back_card),
            contentDescription = "Dos de carte",
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.GIANT.dp),
            contentScale = ContentScale.FillWidth,
        )
    }   
}
