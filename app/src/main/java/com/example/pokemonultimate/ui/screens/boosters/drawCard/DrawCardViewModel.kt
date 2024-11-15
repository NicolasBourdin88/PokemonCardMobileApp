package com.example.pokemonultimate.ui.screens.boosters.drawCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawCardViewModel : ViewModel() {

    val cardToDisplay: MutableList<PokemonCardEntity> = mutableListOf()

    fun getCardsToDraw(setId: String) {
        getAllCollection(
            setId = setId,
            onFinish = { listCard ->
                cardToDisplay.addAll(listCard.filter { it.supertype == PokemonCardEntity.SuperType.ENERGY })
            }
        )
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
