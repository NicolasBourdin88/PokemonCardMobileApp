package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SetEntity(
    val id: String,
    val name: String,
    val printedTotal: Int,
    val total: Int,
    val images: ImageSetEntity,
)
