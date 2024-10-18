package com.example.pokemonultimate.ui.screens.boosters

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.utils.TitleText
import kotlinx.coroutines.delay

@Preview
@Composable
fun BoostersScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TitleText("Want To Open A Booster ?")
        Booster2(height = 450, width = 200)
    }
}

@Preview
@Composable
fun Booster2(height: Int = 450, width: Int = 200) {
    val swipeThreshold = 100f
    var isSwipe: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > swipeThreshold || dragAmount < -swipeThreshold) {
                        isSwipe = true
                    }
                }
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .height(height.dp)
                .width(width.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                BottomBooster(height, width)
            }
            TopBooster(isSwipe)
        }
    }
}

@Preview
@Composable
fun BottomBooster(height: Int = 450, width: Int = 200) {
    Column {
        Quadrilateral(
            modifier = Modifier
                .height((height - 75).dp)
                .width(width.dp)
                .padding(horizontal = 4.dp)
        )
        Image(
            painter = painterResource(R.drawable.piece_of_boosters),
            contentDescription = "Piece in aluminium of booster",
            modifier = Modifier
                .fillMaxWidth()
                .rotate(180F),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun TopBooster(isSwipe: Boolean) {
    Log.e("nicolas", "TopBooster recomposition")
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
        Image(
            painter = painterResource(R.drawable.piece_of_boosters),
            contentDescription = "Piece in aluminium of booster",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        SecondQuadrilateral(modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@Composable
fun Booster(height: Int = 350, width: Int = 200) {
    val swipeThreshold = 100f
    var isSwipe: Boolean by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > swipeThreshold || dragAmount < -swipeThreshold) {
                        isSwipe = true
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .width(width.dp),
        ) {
            var rotate: Float by remember { mutableFloatStateOf(0f) }
            if (isSwipe) {
                LaunchedEffect(Unit) {
                    while (rotate < 10f) {
                        rotate += if (rotate < 0.5f) 0.3f else 0.5f
                        delay(10)
                    }
                }
            }
            Image(
                painter = painterResource(R.drawable.piece_of_boosters),
                contentDescription = "Piece in aluminium of booster",
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(rotate),
                contentScale = ContentScale.FillWidth
            )
            Box(
                modifier = Modifier
                    .height(height.dp)
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth(),
            ) {
                SecondQuadrilateral(modifier = Modifier.rotate(rotate))
                Quadrilateral()
            }
            Image(
                painter = painterResource(R.drawable.piece_of_boosters),
                contentDescription = "Piece in aluminium of booster",
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(180F),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}


@Composable
fun Quadrilateral(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, size.height)
            lineTo(size.width, 30f)
            lineTo(0f, 70f)
            close()
        }

        drawPath(
            path = path,
            color = Color.Blue
        )
    }
}

@Composable
fun SecondQuadrilateral(modifier: Modifier = Modifier) {
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
            color = Color.Yellow
        )
    }
}
