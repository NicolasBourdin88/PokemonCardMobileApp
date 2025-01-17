package com.example.pokemonultimate.data.utils

import android.content.Context
import androidx.compose.material3.DatePickerState
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.pokemonultimate.data.model.database.DataBase
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
    fun providePokemonCardsDatabase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context = context,
            klass = DataBase::class.java,
            name = "pokemonCard.db"
        ).build()
    }
}
