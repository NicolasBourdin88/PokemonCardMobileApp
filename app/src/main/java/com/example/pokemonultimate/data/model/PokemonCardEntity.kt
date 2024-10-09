package com.example.pokemonultimate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity
@Serializable
data class PokemonCardEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val supertype: SuperType,
    val subtypes: List<SubTypeEntity>? = null,
    val types: List<TypeEntity>? = null,
    val set: SetEntity,
    val number: String,
    val artist: String? = null,
    val rarity: String? = null,
    val images: ImagePokemonEntity,
    @SerialName("tcgplayer")
    val tcgPlayer: TcgPlayerEntity? = null,
) {
    @Serializable
    enum class SuperType {
        @SerialName("Energy")
        ENERGY,

        @SerialName("Pokémon")
        POKEMON,

        @SerialName("Trainer")
        TRAINER,
    }

    object Converters {
        @TypeConverter
        fun fromSuperTypeToJson(superType: SuperType?): String? {
            return superType?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToSuperType(json: String?): SuperType? {
            return json?.let { Json.decodeFromString(it) }
        }

        @TypeConverter
        fun fromSubTypeEntityListToJson(subtypes: List<SubTypeEntity>?): String? {
            return subtypes?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToSubTypeEntityList(json: String?): List<SubTypeEntity>? {
            return json?.let { Json.decodeFromString(it) }
        }

        @TypeConverter
        fun fromTypeEntityListToJson(types: List<TypeEntity>?): String? {
            return types?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToTypeEntityList(json: String?): List<TypeEntity>? {
            return json?.let { Json.decodeFromString(it) }
        }

        @TypeConverter
        fun fromSetEntityToJson(set: SetEntity?): String? {
            return set?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToSetEntity(json: String?): SetEntity? {
            return json?.let { Json.decodeFromString(it) }
        }

        @TypeConverter
        fun fromImagePokemonEntityToJson(image: ImagePokemonEntity?): String? {
            return image?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToImagePokemonEntity(json: String?): ImagePokemonEntity? {
            return json?.let { Json.decodeFromString(it) }
        }

        @TypeConverter
        fun fromTcgPlayerEntityToJson(tcgPlayer: TcgPlayerEntity?): String? {
            return tcgPlayer?.let { Json.encodeToString(it) }
        }

        @TypeConverter
        fun fromJsonToTcgPlayerEntity(json: String?): TcgPlayerEntity? {
            return json?.let { Json.decodeFromString(it) }
        }
    }
}
