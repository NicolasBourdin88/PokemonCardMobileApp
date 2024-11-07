package com.example.pokemonultimate.ui.screens.boosters

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

const val BOOSTER_WIDTH = 250
const val BOOSTER_TOP_HEIGHT = 25
const val BOOSTER_BOTTOM_HEIGHT = 370
const val BOOSTER_ALUMINIUM_HEIGHT = 50

@Preview
@Composable
fun BoostersScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TitleText("Want To Open A Booster ?")
        Booster()
    }
}

@Composable
fun Booster() {
    val swipeThreshold = 500f
    var hasSwiped: Boolean by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }
    val swipeProgress = (swipeOffset.absoluteValue / swipeThreshold).coerceIn(0f, 1f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Padding.MEDIUM.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        swipeOffset += dragAmount

                        if (swipeOffset.absoluteValue > swipeThreshold) {
                            hasSwiped = true
                        }
                    },
                    onDragEnd = {
                        swipeOffset = 0f
                    }
                )
            },
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(
                Modifier
                    .width(BOOSTER_WIDTH.dp)
            ) {
                BottomBooster()
                TopBooster(hasSwiped)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(-4.5F)
                    .padding(top = 55.dp),
                contentAlignment = Alignment.Center
            ) {
                if (swipeProgress > 0) {
                    Row(
                        modifier = Modifier
                            .width(BOOSTER_WIDTH.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = Padding.MINI.dp)
                                .height(2.dp)
                                .width((BOOSTER_WIDTH * swipeProgress).dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFF4500),
                                            Color(0xFFFF8C00),
                                            Color(0xFFFFD700),
                                            Color(0xFFFFFFE0),
                                        )
                                    )
                                )
                        )
                        val preloaderLottieComposition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(R.raw.spark_animation_lottie)
                        )
                        LottieAnimation(
                            composition = preloaderLottieComposition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                } else if (!hasSwiped) {
                    val opacity = remember { Animatable(0f) }

                    LaunchedEffect(Unit) {
                        opacity.animateTo(
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 1000
                                ),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.place_holder_cut),
                        contentDescription = "Place holder cut",
                        modifier = Modifier.graphicsLayer(alpha = opacity.value)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBooster() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(top = BOOSTER_ALUMINIUM_HEIGHT.dp)
        ) {
            BottomBoosterContentPart()
            AluminiumBoosterPiece(modifier = Modifier.rotate(180F))
        }
    }
}

@Composable
fun TopBooster(isSwipe: Boolean) {
    var rotate: Float by remember { mutableFloatStateOf(0f) }

    if (isSwipe) {
        LaunchedEffect(Unit) {
            while (rotate < 10f) {
                rotate += if (rotate < 0.5f) 0.3f else 0.5f
                delay(10)
            }
        }
    }

    Column(modifier = Modifier.rotate(rotate)) {
        AluminiumBoosterPiece()
        TopBoosterContentPart()
    }
}

@Preview
@Composable
fun BottomBoosterContentPart() {
    val colorStops = arrayOf(
        0.3f to Color(0XFF77B4FB),
        1f to Color(0XFF130086),
    )

    Column(
        Modifier
            .padding(horizontal = 4.dp)
            .width(BOOSTER_WIDTH.dp)
    ) {
        Canvas(
            modifier = Modifier
                .height(BOOSTER_TOP_HEIGHT.dp)
                .fillMaxWidth()
        ) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(size.width, size.height)
                lineTo(size.width, 0f)
                close()
            }

            drawPath(path = path, color = Color(0XFF77B4FB))
        }


        Column(
            Modifier
                .height(BOOSTER_BOTTOM_HEIGHT.dp)
                .background(brush = Brush.linearGradient(colorStops = colorStops))
        ) {
            BoosterIllustration()
        }
    }
}

@Composable
private fun BoosterIllustration() {
    Column {
        Image(
            painter = painterResource(R.drawable.pokemon_logo),
            contentDescription = "Logo pokemon",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding.HUGE.dp),
        )
        Image(
            painter = painterResource(R.drawable.image_card_lightning),
            contentDescription = "Illustration booster",
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Padding.HUGE.dp),
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Composable
fun TopBoosterContentPart() {
    Canvas(
        modifier = Modifier
            .padding(horizontal = Padding.MICRO.dp)
            .fillMaxWidth()
            .height(BOOSTER_TOP_HEIGHT.dp)
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, size.height)
            lineTo(size.width, 0f)
            close()
        }

        drawPath(
            path = path,
            color = Color(0XFF77B4FB),
        )
    }
}

@Composable
private fun AluminiumBoosterPiece(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.piece_of_boosters),
        contentDescription = "Piece in aluminium of booster",
        modifier = modifier
            .height(BOOSTER_ALUMINIUM_HEIGHT.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.FillBounds
    )
}
