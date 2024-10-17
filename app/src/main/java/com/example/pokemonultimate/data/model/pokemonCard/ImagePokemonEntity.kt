package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ImagePokemonEntity(
    val small: String,
    val large: String,
)
