package com.example.pokemonultimate.ui.screens.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import kotlinx.serialization.json.Json
import androidx.compose.runtime.State

class CardViewModel : ViewModel() {

    private val _checkedRotation = mutableStateOf(false)
    val checkedRotation: State<Boolean> = _checkedRotation

    fun setCheckedRotation(checked: Boolean) {
        _checkedRotation.value = checked
    }

    fun getCardFromJson(jsonCard: String): PokemonCardEntity {
        return Json.decodeFromString<PokemonCardEntity>(jsonCard)
    }

}