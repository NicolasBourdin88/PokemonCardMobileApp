package com.example.pokemonultimate.ui.screens.boosters

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.utils.TitleText

@Preview
@Composable
fun BoostersScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TitleText("Want To Open A Booster ?")
        Booster(height = 350, width = 200)
    }
}

@Composable
fun Booster(height: Int = 350, width: Int = 200) {
    Column(
        modifier = Modifier
            .width(width.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.piece_of_boosters),
            contentDescription = "Piece in aluminium of booster",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .height(height.dp)
                .padding(horizontal = 2.dp)
                .fillMaxWidth(),
        ) {
            SecondQuadrilateral()
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


@Composable
fun Quadrilateral() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
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

@Preview
@Composable
fun SecondQuadrilateral() {
    Canvas(
        modifier = Modifier
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

@Preview
@Composable
fun PreviewQuadrilateral() {
    Quadrilateral()
}
