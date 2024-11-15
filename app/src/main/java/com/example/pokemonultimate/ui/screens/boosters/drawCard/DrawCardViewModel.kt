package com.example.pokemonultimate.ui.screens.boosters.drawCard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrawCardViewModel : ViewModel() {
    private val _cardToDisplay = MutableStateFlow<List<PokemonCardEntity>>(emptyList())
    val cardToDisplay: StateFlow<List<PokemonCardEntity>> get() = _cardToDisplay


    fun getCardsToDraw(setId: String) {
        getAllCollection(
            setId = setId,
            onFinish = { listCard ->
                _cardToDisplay.update { getCardToOpen(listCard) }
            }
        )
    }

    private fun getCardToOpen(listCard: List<PokemonCardEntity>): List<PokemonCardEntity> {
        val boosterContent = mutableListOf<PokemonCardEntity>()

        val cardEnergy = listCard.filter {
            it.supertype == PokemonCardEntity.SuperType.ENERGY
        }

        boosterContent.add(cardEnergy.random())

        repeat(5) {
            boosterContent.add(listCard.random())
        }
        return boosterContent
    }

    private fun getAllCollection(setId: String, onFinish: (List<PokemonCardEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val allCards = mutableListOf<PokemonCardEntity>()
            var currentPage = 1
            var hasMorePages = true

            while (hasMorePages) {
                val response: ApiResponse<List<PokemonCardEntity>> = ApiRepository.getCardsFromSet(
                    setId = setId,
                    page = currentPage,
                    pageSize = 250
                )

                response.data?.let { cards ->
                    allCards.addAll(cards)

                    val totalCount = response.totalCount
                    val countSoFar = allCards.size
                    hasMorePages = countSoFar < totalCount

                    currentPage++
                } ?: {
                    hasMorePages = false
                }
            }
            onFinish(allCards)
        }
    }
}
