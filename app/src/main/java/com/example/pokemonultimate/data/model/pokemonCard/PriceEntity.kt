package com.example.pokemonultimate.data.model.pokemonCard

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class PriceEntity(
    @SerialName("holofoil")
    val prices: PricesEntity? = null
)