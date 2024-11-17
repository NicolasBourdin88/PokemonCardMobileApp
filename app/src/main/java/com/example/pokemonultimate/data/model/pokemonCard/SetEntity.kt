package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import com.example.pokemonultimate.data.model.sets.ImageSetEntity
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