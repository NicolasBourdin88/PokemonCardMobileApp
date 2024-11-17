package com.example.pokemonultimate.data.model.userModel.database

import androidx.room.TypeConverter
import com.example.pokemonultimate.data.model.PokemonCellProfile

class PokemonCellProfileConverter {
    @TypeConverter
    fun fromPokemonCellProfile(profile: PokemonCellProfile): String {
        return profile.name
    }

    @TypeConverter
    fun toPokemonCellProfile(value: String): PokemonCellProfile {
        return PokemonCellProfile.valueOf(value)
    }
}