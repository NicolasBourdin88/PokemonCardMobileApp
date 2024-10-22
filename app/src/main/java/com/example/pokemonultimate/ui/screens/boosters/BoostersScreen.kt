package com.example.pokemonultimate.ui.screens.boosters

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.TitleText
import kotlinx.coroutines.delay

@Preview
@Composable
fun BoostersScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TitleText("Want To Open A Booster ?")
        Booster(height = 500, width = 250)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = {
                // TODO
            }) {
                Text("Open", modifier = Modifier.padding(horizontal = Padding.LARGE.dp))
            }
        }
    }
}

@Composable
fun Booster(height: Int = 500, width: Int = 250) {
    val swipeThreshold = 100f
    var isSwipe: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Padding.MEDIUM.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > swipeThreshold || dragAmount < -swipeThreshold) {
                        isSwipe = true
                    }
                }
            },
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(
                Modifier
                    .height(height.dp)
                    .width(width.dp)
            ) {
                BottomBooster(height, width)
                TopBooster(isSwipe)
            }
            Image(
                painter = painterResource(R.drawable.place_holder_cut),
                contentDescription = "Place holder cut",
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(-3.5F)
                    .padding(top = 55.dp)
            )
        }
    }
}

@Composable
fun BottomBooster(height: Int = 500, width: Int = 250) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        BottomBoosterContent(
            modifier = Modifier
                .height((height - 97).dp)
                .width(width.dp)
                .padding(horizontal = 4.dp)
        )
        AluminiumBoosterPiece(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(180F)
        )
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
        AluminiumBoosterPiece(modifier = Modifier.fillMaxWidth())
        TopBoosterPart(modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@Composable
fun BottomBoosterContent(modifier: Modifier = Modifier) {
    Box {
        Canvas(modifier = modifier) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(size.width, size.height)
                lineTo(size.width, 30f)
                lineTo(0f, 70f)
                close()
            }

            val colorStops = arrayOf(
                0.3f to Color(0XFF77B4FB),
                1f to Color(0XFF130086),
            )

            drawPath(
                path = path,
                brush = Brush.linearGradient(colorStops = colorStops)
            )
        }

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
}

@Composable
fun TopBoosterPart(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, 70f)
            lineTo(size.width, 30f)
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
private fun AluminiumBoosterPiece(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.piece_of_boosters),
        contentDescription = "Piece in aluminium of booster",
        modifier = modifier,
        contentScale = ContentScale.FillWidth
    )
}
