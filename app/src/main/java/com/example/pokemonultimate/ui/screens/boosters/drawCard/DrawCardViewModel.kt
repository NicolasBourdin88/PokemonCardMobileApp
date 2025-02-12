package com.example.pokemonultimate.ui.screens.boosters.drawCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.isBeforeFinalBoosterCard
import com.example.pokemonultimate.data.model.pokemonCard.isCommonBoosterCard
import com.example.pokemonultimate.data.model.pokemonCard.isFinalBoosterCard
import com.example.pokemonultimate.data.utils.KEY_FIREBASE_COLLECTION
import com.example.pokemonultimate.data.utils.getUserCards
import com.example.pokemonultimate.data.utils.getUserId
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawCardViewModel @Inject constructor(private val pokemonCardsDb: DataBase) : ViewModel() {

    private val _cardToDisplay = MutableStateFlow<List<PokemonCardEntity>>(emptyList())
    val cardToDisplay: StateFlow<List<PokemonCardEntity>> get() = _cardToDisplay

    private val _displaySet = MutableStateFlow("swsh1")
    val displaySet: StateFlow<String> get() = _displaySet

    private val _displaySetImage = MutableStateFlow("swsh1")
    val displaySetImage: StateFlow<String> get() = _displaySetImage

    init {
        setSet()
    }

    fun getCardsToDraw(setId: String) {
        getAllCollection(
            setId = setId,
            onFinish = { listCard ->
                _cardToDisplay.update { getCardToOpen(listCard) }
            }
        )
    }

    fun setSet() {
        _cardToDisplay.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            val set = pokemonCardsDb.setDao.getAllSets().random()
            _displaySet.value = set.id
            _displaySetImage.value = set.images.logo
        }
    }

    private fun getCardToOpen(listCard: List<PokemonCardEntity>): List<PokemonCardEntity> {
        val boosterContent = mutableListOf<PokemonCardEntity>()

        val cardEnergyList = listCard.filter {
            it.supertype == PokemonCardEntity.SuperType.ENERGY
        }
        val commonCardList = listCard.filter {
            it.rarity?.isCommonBoosterCard() == true
        }
        val endCardList = listCard.filter {
            it.rarity?.isFinalBoosterCard() == true
        }
        val secondToLastCardList = listCard.filter {
            it.rarity?.isBeforeFinalBoosterCard() == true
        }

        if (endCardList.isNotEmpty()) {
            boosterContent.add(endCardList.random())
        } else {
            boosterContent.add(listCard.random())
        }

        if (secondToLastCardList.isNotEmpty()) {
            boosterContent.add(secondToLastCardList.random())
        } else {
            boosterContent.add(listCard.random())
        }

        val numberOfCommonToGet = if (cardEnergyList.isEmpty()) 9 else 8
        boosterContent.addAll(listCard.shuffled().take(numberOfCommonToGet))

        if (cardEnergyList.isNotEmpty()) {
            boosterContent.add(cardEnergyList.random())
        } else {
            boosterContent.add(listCard.random())
        }

        saveDrawnCard(boosterContent)

        return boosterContent
    }

    private fun saveDrawnCard(drawnCards: List<PokemonCardEntity>) {
        val db = FirebaseFirestore.getInstance()
        val userId = getUserId() ?: return
        val userDocumentRef = db.collection("users").document(userId)

        getUserCards(onFinish = { userCards, documentExists ->
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
