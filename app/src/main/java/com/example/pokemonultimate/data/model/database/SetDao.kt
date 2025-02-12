package com.example.pokemonultimate.data.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonultimate.data.model.sets.Set

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(listSets: List<Set>)

    @Query("SELECT * FROM `Set`")
    suspend fun getAllSets(): List<Set>
}
