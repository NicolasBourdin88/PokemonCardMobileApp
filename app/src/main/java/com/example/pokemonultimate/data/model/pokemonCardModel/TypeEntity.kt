package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
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
