package com.example.pokemonultimate.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Brush
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.theme.*


enum class PokemonCellProfil(
    @DrawableRes val pokemonCellImage: Int,
    val brush: Brush,
) {
    ICE(
        pokemonCellImage = R.drawable.image_card_ice,
        brush = Brush.linearGradient(listOf(cardIceFirstColor, cardIceSecondColor)),

        ),
    FIRE(
        pokemonCellImage = R.drawable.image_card_fire,
        brush = Brush.linearGradient(listOf(cardFireFirstColor, cardFireSecondColor)),

        ),
    PLANT(
        pokemonCellImage = R.drawable.image_card_grass,
        brush = Brush.linearGradient(listOf(cardGrassFirstColor, cardGrassSecondColor)),

        ),
    ELECTRIC(
        pokemonCellImage = R.drawable.image_card_lightning,
        brush = Brush.linearGradient(listOf(cardLightningFirstColor, cardLightningSecondColor)),

        ),
    WATER(
        pokemonCellImage = R.drawable.image_card_water,
        brush = Brush.linearGradient(listOf(cardWaterFirstColor, cardWaterSecondColor)),

        ),
    NORMAL(
        pokemonCellImage = R.drawable.image_card_normal,
        brush = Brush.linearGradient(listOf(cardNormalFirstColor, cardNormalSecondColor)),
    ),

}
