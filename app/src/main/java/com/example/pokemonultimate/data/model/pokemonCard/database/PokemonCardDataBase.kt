package com.example.pokemonultimate.data.model.pokemonCard.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.RemoteKey

@Database(entities = [PokemonCardEntity::class, RemoteKey::class], version = 1)

@TypeConverters(PokemonCardEntity.Converters::class)
abstract class PokemonCardDataBase : RoomDatabase() {
    abstract val dao: PokemonDao
    abstract val remoteKeyDao: RemoteKeyDao
}
