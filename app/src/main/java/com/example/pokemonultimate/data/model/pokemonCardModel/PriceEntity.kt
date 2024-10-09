package com.example.pokemonultimate.data.model.pokemonCardModel

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class PriceEntity(
    @SerialName("holofoil")
    val prices: PricesEntity? = null
)
