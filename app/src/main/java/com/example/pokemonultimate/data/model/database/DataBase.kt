package com.example.pokemonultimate.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.RemoteKey
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.data.model.userModel.database.PokemonCellProfileConverter

@Database(entities = [PokemonCardEntity::class, RemoteKey::class, UserProfile::class], version = 1)

@TypeConverters(PokemonCardEntity.Converters::class,PokemonCellProfileConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract val dao: PokemonDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val userProfileDao: UserProfileDao
}