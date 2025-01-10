package com.example.pokemonultimate.ui.screens.boosters.drawCard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.ui.utils.Padding
import kotlinx.coroutines.delay

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun DrawCardScreen(
    setId: String, drawCardViewModel: DrawCardViewModel = viewModel(), onFinish: () -> Unit
) {
    LaunchedEffect(Unit) { drawCardViewModel.getCardsToDraw(setId) }

    val cards by drawCardViewModel.cardToDisplay.collectAsState()
    var indexCardToDisplay by rememberSaveable { mutableIntStateOf(1) }
    val textButton = remember(indexCardToDisplay) { getButtonText(cards, indexCardToDisplay) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val shouldDisplayResumed = indexCardToDisplay > cards.size && cards.isNotEmpty()
        Box(modifier = Modifier.animateContentSize()) {
            if (shouldDisplayResumed) {
                DisplayResume(cards)
            } else {
                DisplayDrawnCard(cards, indexCardToDisplay)
            }
        }

        Button(
            onClick = {
                if (textButton != "Add to collection") {
                    indexCardToDisplay++
                } else {
                    onFinish.invoke()
                }
            },
            modifier = Modifier
                .padding(
                    top = Padding.GIANT.dp,
                    bottom = 0.dp,
                    start = Padding.HUGE.dp,
                    end = Padding.HUGE.dp
                )
                .fillMaxWidth(),
            enabled = cards.isNotEmpty(),
        ) {
            Text(textButton)
        }
    }
}

@Composable
private fun DisplayResume(cards: List<PokemonCardEntity>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier.padding(horizontal = Padding.MINI.dp),
    ) {
        items(cards) {
            AsyncImage(
                model = it.images.large,
                contentDescription = it.name,
                modifier = Modifier
                    .padding(Padding.MICRO.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

fun getButtonText(listCards: List<PokemonCardEntity>, indexCardToDisplay: Int): String {
    return if (listCards.isEmpty() || indexCardToDisplay < listCards.lastIndex + 1) {
        "Next card"
    } else if (indexCardToDisplay == listCards.size) {
        "Show result"
    } else {
        "Add to collection"
    }
}

@Composable
private fun DisplayDrawnCard(cards: List<PokemonCardEntity>, indexCardToDisplay: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.GIANT.dp)
    ) {
        cards.forEachIndexed { index, pokemonCardEntity ->
            if (index != cards.lastIndex) {
                DisplayCard(indexCardToDisplay, cards.size, index, pokemonCardEntity)
            }
        }

        DisplayFirstCard(cards, indexCardToDisplay)
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun DisplayCard(
    indexCardToDisplay: Int,
    numberOfCards: Int,
    currentCardIndex: Int,
    pokemonCardEntity: PokemonCardEntity
) {
    val shouldDisplayNextCard = indexCardToDisplay > numberOfCards - currentCardIndex
    val offsetX by animateDpAsState(
        targetValue = if (shouldDisplayNextCard) 500.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val offsetY by animateDpAsState(
        targetValue = if (shouldDisplayNextCard) (-100).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val rotationAngleZ by animateFloatAsState(
        targetValue = if (shouldDisplayNextCard) 15f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val rotationAngleX by animateFloatAsState(
        targetValue = if (shouldDisplayNextCard) -30f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    AsyncImage(
        model = pokemonCardEntity.images.large,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX, y = offsetY)
            .graphicsLayer {
                rotationZ = rotationAngleZ
                rotationY = rotationAngleX
            },
        placeholder = painterResource(R.drawable.back_card),
        contentScale = ContentScale.FillWidth,
    )
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun DisplayFirstCard(cards: List<PokemonCardEntity>, indexCardToDisplay: Int) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(cards.firstOrNull()) {
        cards.lastOrNull()?.images?.large?.let { imageUrl ->
            val request = ImageRequest.Builder(context).data(imageUrl).build()
            val result = imageLoader.execute(request)
            bitmap = (result.drawable as? BitmapDrawable)?.bitmap
        }
    }

    val offsetX by animateDpAsState(
        targetValue = if (indexCardToDisplay > 1) 500.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val offsetY by animateDpAsState(
        targetValue = if (indexCardToDisplay > 1) (-100).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val rotationAngleZ by animateFloatAsState(
        targetValue = if (indexCardToDisplay > 1) 15f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    val rotationAngleX by animateFloatAsState(
        targetValue = if (indexCardToDisplay > 1) -30f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )


    if (cards.isNotEmpty() && bitmap != null) {
        var cardFace by remember { mutableStateOf(CardFace.Back) }

        LaunchedEffect(cardFace) {
            delay(500)
            cardFace = CardFace.Front
        }

        FlipCard(cardFace = cardFace,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX, y = offsetY)
                .graphicsLayer {
                    rotationZ = rotationAngleZ
                    rotationY = rotationAngleX
                },
            front = {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            },
            back = {
                Image(
                    painter = painterResource(R.drawable.back_card),
                    contentDescription = "Dos de carte",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            })
    } else {
        Image(
            painter = painterResource(R.drawable.back_card),
            contentDescription = "Dos de carte",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
    }
}
