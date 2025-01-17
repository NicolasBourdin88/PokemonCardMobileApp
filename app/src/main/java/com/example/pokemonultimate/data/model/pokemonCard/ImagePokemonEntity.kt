package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ImagePokemonEntity(
    val small: String,
    val large: String,
) {
    constructor() : this("", "")

    companion object {
        fun mapToImagePokemonEntity(map: Map<String, Any>): ImagePokemonEntity {
            return ImagePokemonEntity(
                small = map["small"] as? String ?: "",
                large = map["large"] as? String ?: ""
            )
        }
    }
}
