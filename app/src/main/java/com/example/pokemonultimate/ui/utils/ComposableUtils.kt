package com.example.pokemonultimate.ui.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonultimate.R

val fontFamilyAvenir = FontFamily(
    Font(R.font.avenir_book, FontWeight.Bold),
    Font(R.font.avenir_black, FontWeight.Black),
    Font(R.font.avenir_heavy, FontWeight.ExtraBold),
    Font(R.font.avenir_light, FontWeight.Light),
    Font(R.font.avenir_regular, FontWeight.Normal)
)

@Composable
fun TitleText(label: String, modifier: Modifier = Modifier) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onTertiaryContainer
    )

    Text(
        text = label,
        fontFamily = fontFamilyAvenir,
        modifier = modifier.padding(horizontal = 24.dp),
        fontSize = 40.sp,
        fontWeight = FontWeight.Black,
        style = TextStyle(
            brush = Brush.linearGradient(gradientColors),
            fontSize = 30.sp
        ),
        lineHeight = 40.sp,
    )
}