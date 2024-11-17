package com.example.pokemonultimate.ui.screens.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.database.PokemonCardRemoteMediator
import com.example.pokemonultimate.data.model.sets.Set
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val pokemonCardsDb: DataBase) : ViewModel() {

    private val _setsFlow = MutableStateFlow<List<Set>>(emptyList())
    val setsFlow: StateFlow<List<Set>> = _setsFlow

    init {
        getSets()
    }

    private fun getSets() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResponse: ApiResponse<List<Set>> = ApiRepository.getSets()
            _setsFlow.value = apiResponse.data ?: emptyList()
        }
    }

    fun getFlow(
        setId: String,
    ): Flow<PagingData<PokemonCardEntity>> {
        return getPager(
            setId = setId,
        ).flow.map { pagingData ->
            pagingData.map { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPager(
        setId: String,
    ): Pager<Int, PokemonCardEntity> {
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
}