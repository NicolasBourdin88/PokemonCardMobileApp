package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import com.example.pokemonultimate.data.model.pokemonCard.TcgPlayerEntity.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Entity
@Serializable
class CardMarketEntity (
    val url: String,
    @Serializable(DateSerializer::class)
    val updatedAt: Date,
    val prices: PriceEntityMarket? = null,
) {

}