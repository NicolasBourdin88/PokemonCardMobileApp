package com.example.pokemonultimate.data.model.sets

import androidx.room.Entity
import androidx.room.TypeConverter
import com.example.pokemonultimate.data.model.pokemonCard.ImagePokemonEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

        class ImageSetConverter {
            private val json = Json

            @TypeConverter
            fun fromImageSetEntity(value: ImageSetEntity): String {
                return json.encodeToString(value)
            }

            @TypeConverter
            fun toImageSetEntity(value: String): ImageSetEntity {
                return json.decodeFromString(value)
            }
        }
    }
}
