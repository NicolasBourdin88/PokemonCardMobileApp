package com.example.pokemonultimate.data.model.sets

import kotlinx.serialization.Serializable

@Serializable
data class pokemonSet(
    val id: String,
    val name: String,
    val series: String,
    val total: Int,
    val releaseDate: String,
    val updatedAt: String,
    val image: ImageSetEntity
    )
