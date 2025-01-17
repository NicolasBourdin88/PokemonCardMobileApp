package com.example.pokemonultimate.ui.screens.boosters.drawCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.isBeforeFinalBoosterCard
import com.example.pokemonultimate.data.model.pokemonCard.isCommonBoosterCard
import com.example.pokemonultimate.data.model.pokemonCard.isFinalBoosterCard
import com.example.pokemonultimate.data.utils.KEY_FIREBASE_COLLECTION
import com.example.pokemonultimate.data.utils.getUserCards
import com.example.pokemonultimate.data.utils.getUserId
import com.google.firebase.firestore.FirebaseFirestore
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

        val cardEnergyList = listCard.filter {
            it.supertype == PokemonCardEntity.SuperType.ENERGY
        }
        val commonCardList = listCard.filter {
            it.rarity.isCommonBoosterCard()
        }
        val endCardList = listCard.filter {
            it.rarity.isFinalBoosterCard()
        }
        val secondToLastCardList = listCard.filter {
            it.rarity.isBeforeFinalBoosterCard()
        }

        if (endCardList.isNotEmpty()) {
            boosterContent.add(endCardList.random())
        } else {
            boosterContent.add(commonCardList.random())
        }

        if (secondToLastCardList.isNotEmpty()) {
            boosterContent.add(secondToLastCardList.random())
        } else {
            boosterContent.add(commonCardList.random())
        }

        val numberOfCommonToGet = if (cardEnergyList.isEmpty()) 9 else 8
        boosterContent.addAll(commonCardList.shuffled().take(numberOfCommonToGet))

        if (cardEnergyList.isNotEmpty()) {
            boosterContent.add(cardEnergyList.random())
        } else {
            boosterContent.add(commonCardList.random())
        }

        saveDrawnCard(boosterContent)

        return boosterContent
    }

    private fun saveDrawnCard(drawnCards: List<PokemonCardEntity>) {
        val db = FirebaseFirestore.getInstance()
        val userId = getUserId() ?: return
        val userDocumentRef = db.collection("users").document(userId)

        getUserCards(userDocumentRef, onFinish = { userCards, documentExists ->
            if (documentExists) {
                drawnCards.forEach { drawnCard ->
                    if (!userCards.contains(drawnCard)) userCards.add(drawnCard)
                }

                userDocumentRef.update(KEY_FIREBASE_COLLECTION, userCards.toList())
            } else {
                userDocumentRef.set(mapOf(KEY_FIREBASE_COLLECTION to drawnCards.toList()))
            }
        })
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
