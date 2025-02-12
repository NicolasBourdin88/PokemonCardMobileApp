package com.example.pokemonultimate.ui.screens.collection

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.database.PokemonCardRemoteMediator
import com.example.pokemonultimate.data.model.sets.Set
import com.example.pokemonultimate.data.utils.NetworkAvailability
import com.example.pokemonultimate.data.utils.getUserCards
import com.example.pokemonultimate.data.utils.getUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    application: Application,
    private val pokemonCardsDb: DataBase
) : ViewModel() {

    private val _setsFlow = MutableStateFlow<List<Set>>(emptyList())
    val setsFlow: StateFlow<List<Set>> = _setsFlow
    private var userCards: kotlin.collections.Set<PokemonCardEntity> = emptySet()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isNetworkAvailable = NetworkAvailability(application).isNetworkAvailable
        .mapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    init {
        getSets()
        getUserCards()
    }

    private fun getUserCards() {
        getUserCards(onFinish = { cards, _ ->
            userCards = cards
        })
    }


    private fun getSets() {
        viewModelScope.launch {
            _setsFlow.value = pokemonCardsDb.setDao.getAllSets()
        }
    }

    fun getFlow(setId: String): Flow<PagingData<PokemonCardEntity>> {
        return getPager(
            setId = setId,
        ).flow.map { pagingData ->
            pagingData.map { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPager(setId: String): Pager<Int, PokemonCardEntity> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = PokemonCardRemoteMediator(
                pokemonCardDb = pokemonCardsDb,
                setId = setId,
            ),
            pagingSourceFactory = {
                pokemonCardsDb.dao.pagingSource()
            }
        )
    }

    fun getNumberOfCardsInSet(setId: String): Int {
        getUserId() ?: return 0
        val count = userCards.count { setId == it.set.id }
        Log.e("nicolas", "getNumberOfCardsInSet - setId: ${setId}")
        if (setId == "swsh1") {
            Log.e("nicolas", "getNumberOfCardsInSet - $count")
        }
        return count
    }

    fun userHaveCards(card: PokemonCardEntity): Boolean {
        getUserId() ?: return true
        return userCards.count { card.id == it.id } > 0
    }

    fun getOfflineCardsFromSet(setId: String): List<PokemonCardEntity> {
        return userCards.filter { it.set.id == setId }
    }
}
