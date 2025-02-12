package com.example.pokemonultimate.ui.screens.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import kotlinx.serialization.json.Json
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.example.pokemonultimate.R


class CardViewModel : ViewModel() {

    private val _checkedRotation = mutableStateOf(false)
    val checkedRotation: State<Boolean> = _checkedRotation

    fun setCheckedRotation(checked: Boolean) {
        _checkedRotation.value = checked
    }

    fun getCardFromJson(jsonCard: String): PokemonCardEntity {
        return Json.decodeFromString<PokemonCardEntity>(jsonCard)
    }

    fun getPriceIconAndTint(price: Double?, avg30: Double?): Pair<Int, Color> {
        return when {
            price == null || avg30 == null -> R.drawable.ic_equal to Color.Blue
            price > avg30 -> R.drawable.ic_arrow_up to Color.Green
            price < avg30 -> R.drawable.ic_arrow_down to Color.Red
            else -> R.drawable.ic_equal to Color.Blue
        }
    }

}