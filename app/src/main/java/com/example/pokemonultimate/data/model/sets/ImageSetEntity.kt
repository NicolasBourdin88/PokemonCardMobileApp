package com.example.pokemonultimate.data.model.sets

import androidx.room.Entity
import com.example.pokemonultimate.data.model.pokemonCard.ImagePokemonEntity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ImageSetEntity(
    val symbol: String,
    val logo: String,
) {
    constructor() : this("", "")

    companion object {
        fun mapToImageSetEntity(map: Map<String, Any>): ImagePokemonEntity {
            return ImagePokemonEntity(
                small = map["small"] as? String ?: "",
                large = map["large"] as? String ?: ""
            )
        }
    }
}
