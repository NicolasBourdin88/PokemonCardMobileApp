package com.example.pokemonultimate.data.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
class ImagePokemonEntity(
    val small: String,
    val large: String,
)
