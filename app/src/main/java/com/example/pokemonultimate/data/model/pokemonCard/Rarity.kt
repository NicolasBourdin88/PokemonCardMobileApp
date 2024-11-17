package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
enum class Rarity {
    @SerialName("Amazing Rare")
    AMAZING_RARE,

    @SerialName("Common")
    COMMON,

    @SerialName("LEGEND")
    LEGEND,

    @SerialName("Promo")
    PROMO,

    @SerialName("Rare")
    RARE,

    @SerialName("Rare ACE")
    RARE_ACE,

    @SerialName("Rare BREAK")
    RARE_BREAK,

    @SerialName("Rare Holo")
    RARE_HOLO,

    @SerialName("Rare Holo EX")
    RARE_HOLO_EX,

    @SerialName("Rare Holo GX")
    RARE_HOLO_GX,

    @SerialName("Rare Holo LV.X")
    RARE_HOLO_LV_X,

    @SerialName("Rare Holo Star")
    RARE_HOLO_STAR,

    @SerialName("Rare Holo V")
    RARE_HOLO_V,

    @SerialName("Rare Holo VMAX")
    RARE_HOLO_VMAX,

    @SerialName("Rare Prime")
    RARE_PRIME,

    @SerialName("Rare Prism Star")
    RARE_PRISM_STAR,

    @SerialName("Rare Rainbow")
    RARE_RAINBOW,

    @SerialName("Rare Secret")
    RARE_SECRET,

    @SerialName("Rare Shining")
    RARE_SHINING,

    @SerialName("Rare Shiny")
    RARE_SHINY,

    @SerialName("Rare Shiny GX")
    RARE_SHINY_GX,

    @SerialName("Rare Ultra")
    RARE_ULTRA,

    @SerialName("Uncommon")
    UNCOMMON
}

fun Rarity.isFinalBoosterCard(): Boolean {
    return when (this) {
        Rarity.AMAZING_RARE,
        Rarity.LEGEND,
        Rarity.RARE_ACE,
        Rarity.RARE_BREAK,
        Rarity.RARE_HOLO,
        Rarity.RARE_HOLO_EX,
        Rarity.RARE_HOLO_GX,
        Rarity.RARE_HOLO_LV_X,
        Rarity.RARE_HOLO_STAR,
        Rarity.RARE_HOLO_V,
        Rarity.RARE_HOLO_VMAX,
        Rarity.RARE_PRIME,
        Rarity.RARE_PRISM_STAR,
        Rarity.RARE_RAINBOW,
        Rarity.RARE_SECRET,
        Rarity.RARE_SHINING,
        Rarity.RARE_SHINY,
        Rarity.RARE_SHINY_GX,
        Rarity.RARE_ULTRA -> true
        else -> false
    }
}

fun Rarity.isBeforeFinalBoosterCard(): Boolean {
    return when (this) {
        Rarity.RARE_HOLO,
        Rarity.RARE_HOLO_EX,
        Rarity.RARE_HOLO_GX,
        Rarity.RARE_HOLO_LV_X,
        Rarity.RARE_HOLO_STAR,
        Rarity.RARE_HOLO_V,
        Rarity.RARE_HOLO_VMAX -> true
        else -> false
    }
}

fun Rarity.isCommonBoosterCard(): Boolean {
    return when (this) {
        Rarity.COMMON,
        Rarity.UNCOMMON,
        Rarity.PROMO,
        Rarity.RARE -> true
        else -> false
    }
}
