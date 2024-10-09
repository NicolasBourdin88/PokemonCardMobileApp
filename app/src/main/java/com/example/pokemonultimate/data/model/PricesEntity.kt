package com.example.pokemonultimate.data.model

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class PricesEntity(
    val low: Double,
    val mid: Double,
    val high: Double,
    val market: Double,
    val directLow: Double?,
)
