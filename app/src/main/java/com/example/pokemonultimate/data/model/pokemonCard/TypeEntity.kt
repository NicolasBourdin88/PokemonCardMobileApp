package com.example.pokemonultimate.data.model.pokemonCard

import androidx.compose.ui.graphics.Brush
import androidx.room.Entity
import com.example.pokemonultimate.ui.theme.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
enum class TypeEntity {
    @SerialName("Colorless")
    COLORLESS,

    @SerialName("Darkness")
    DARKNESS,

    @SerialName("Dragon")
    DRAGON,

    @SerialName("Fairy")
    FAIRY,

    @SerialName("Fighting")
    FIGHTING,

    @SerialName("Fire")
    FIRE,

    @SerialName("Grass")
    GRASS,

    @SerialName("Lightning")
    LIGHTNING,

    @SerialName("Metal")
    METAL,

    @SerialName("Psychic")
    PSYCHIC,

    @SerialName("Water")
    WATER
}

enum class ColorType(val type: TypeEntity, val brush: Brush) {
    COLORLESS(
        type = TypeEntity.COLORLESS,
        brush = Brush.linearGradient(listOf(cardColorlessFirstColor, cardColorlessSecondColor))
    ),
    DARKNESS(
        type = TypeEntity.DARKNESS,
        brush = Brush.linearGradient(listOf(cardDarknessFirstColor, cardDarknessSecondColor))
    ),
    DRAGON(
        type = TypeEntity.DRAGON,
        brush = Brush.linearGradient(listOf(cardDragonFirstColor, cardDragonSecondColor))
    ),
    FAIRY(
        type = TypeEntity.FAIRY,
        brush = Brush.linearGradient(listOf(cardFairyFirstColor, cardFairySecondColor))
    ),
    FIGHTING(
        type = TypeEntity.FIGHTING,
        brush = Brush.linearGradient(listOf(cardFightingFirstColor, cardFightingSecondColor))
    ),
    FIRE(
        type = TypeEntity.FIRE,
        brush = Brush.linearGradient(listOf(cardFireFirstColor, cardFireSecondColor))
    ),
    GRASS(
        type = TypeEntity.GRASS,
        brush = Brush.linearGradient(listOf(cardGrassFirstColor, cardGrassSecondColor))
    ),
    LIGHTNING(
        type = TypeEntity.LIGHTNING,
        brush = Brush.linearGradient(listOf(cardLightningFirstColor, cardLightningSecondColor))
    ),
    METAL(
        type = TypeEntity.METAL,
        brush = Brush.linearGradient(listOf(cardMetalFirstColor, cardMetalSecondColor))
    ),
    PSYCHIC(
        type = TypeEntity.PSYCHIC,
        brush = Brush.linearGradient(listOf(cardPsychicFirstColor, cardPsychicSecondColor))
    ),
    WATER(
        type = TypeEntity.WATER,
        brush = Brush.linearGradient(listOf(cardWaterFirstColor, cardWaterSecondColor))
    );

    companion object {
        fun fromTypeName(typeName: String): ColorType? {
            return ColorType.entries.find { it.type.name == typeName.uppercase() }
        }
    }
}
