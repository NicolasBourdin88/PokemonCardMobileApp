package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ImageSetEntity(
    val symbol: String,
    val logo: String,
)
