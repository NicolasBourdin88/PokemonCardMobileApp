package com.example.pokemonultimate.data.utils

import android.content.Context
import androidx.room.Room
import com.example.pokemonultimate.data.model.pokemonCard.database.PokemonCardDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun providePokemonCardsDatabase(@ApplicationContext context: Context): PokemonCardDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = PokemonCardDataBase::class.java,
            name = "pokemonCard.db"
        ).build()
    }

}
