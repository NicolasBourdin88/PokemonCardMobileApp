package com.example.pokemonultimate.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Brush
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.theme.cardFireFirstColor
import com.example.pokemonultimate.ui.theme.cardFireSecondColor
import com.example.pokemonultimate.ui.theme.cardGrassFirstColor
import com.example.pokemonultimate.ui.theme.cardGrassSecondColor
import com.example.pokemonultimate.ui.theme.cardIceFirstColor
import com.example.pokemonultimate.ui.theme.cardIceSecondColor
import com.example.pokemonultimate.ui.theme.cardLightningFirstColor
import com.example.pokemonultimate.ui.theme.cardLightningSecondColor
import com.example.pokemonultimate.ui.theme.cardNormalFirstColor
import com.example.pokemonultimate.ui.theme.cardNormalSecondColor
import com.example.pokemonultimate.ui.theme.cardWaterFirstColor
import com.example.pokemonultimate.ui.theme.cardWaterSecondColor

enum class PokemonCellProfile(
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
    LIGHTNING(
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
