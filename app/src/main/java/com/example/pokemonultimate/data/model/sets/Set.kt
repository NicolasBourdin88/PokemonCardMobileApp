package com.example.pokemonultimate.data.model.sets

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Set(
    @PrimaryKey
    val id: String,
    val name: String,
    val series: String,
    val total: Int,
    val releaseDate: String,
    val updatedAt: String,
    val images: ImageSetEntity,
)
