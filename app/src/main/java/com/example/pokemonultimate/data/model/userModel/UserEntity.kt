package com.example.pokemonultimate.data.model.userModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokemonultimate.data.model.PokemonCellProfile

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userId: String,
    val pokemonCell: PokemonCellProfile
)
