package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
class PriceEntityMarket (
    val averageSellPrice: Double,
    val lowPrice: Double,
    val trendPrice: Double,
    val avg30: Double
)