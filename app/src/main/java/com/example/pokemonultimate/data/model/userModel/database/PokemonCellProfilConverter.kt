package com.example.pokemonultimate.data.model.userModel.database

import androidx.room.TypeConverter
import com.example.pokemonultimate.data.model.PokemonCellProfil

class PokemonCellProfilConverter {
    @TypeConverter
    fun fromPokemonCellProfil(profil: PokemonCellProfil): String {
        return profil.name
    }

    @TypeConverter
    fun toPokemonCellProfil(value: String): PokemonCellProfil {
        return PokemonCellProfil.valueOf(value)
    }
}