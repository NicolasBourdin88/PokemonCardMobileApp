package com.example.pokemonultimate.data.model

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PokemonDao {

    @Upsert
    suspend fun upsertAll(pokemonList: List<PokemonCardEntity>)

    @Query("SELECT * FROM pokemoncardentity")
    fun pagingSource(): PagingSource<Int, PokemonCardEntity>

    @Query("DELETE FROM pokemoncardentity")
    suspend fun clearAll()
}
