package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: String,
    val nextKey: Int?,
)
