package com.example.pokemonultimate.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.RemoteKey
import com.example.pokemonultimate.data.model.pokemonCard.SetEntity
import com.example.pokemonultimate.data.model.sets.ImageSetEntity.Companion.ImageSetConverter
import com.example.pokemonultimate.data.model.sets.Set
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.data.model.userModel.database.PokemonCellProfileConverter

@Database(
    entities = [PokemonCardEntity::class, RemoteKey::class, UserProfile::class, Set::class],
    version = 1
)

@TypeConverters(
    PokemonCardEntity.Converters::class,
    PokemonCellProfileConverter::class,
    ImageSetConverter::class
)

abstract class DataBase : RoomDatabase() {
    abstract val dao: PokemonDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val userProfileDao: UserProfileDao
    abstract val setDao: SetDao
}
