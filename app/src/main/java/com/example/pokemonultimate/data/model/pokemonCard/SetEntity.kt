package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokemonultimate.data.model.sets.ImageSetEntity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SetEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val total: Int,
    val images: ImageSetEntity,
) {
    constructor() : this("", "", 0, ImageSetEntity())

    companion object {
        fun mapToSetEntity(map: Map<String, Any>): SetEntity {
            return SetEntity(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                total = (map["total"] as? Long)?.toInt() ?: 0,
                images = ImageSetEntity()
            )
        }
    }
}
