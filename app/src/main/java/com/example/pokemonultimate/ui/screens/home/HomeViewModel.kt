package com.example.pokemonultimate.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.model.PokemonCardDataBase
import com.example.pokemonultimate.data.model.PokemonCardEntity
import com.example.pokemonultimate.data.model.PokemonCardRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonCardsDb: PokemonCardDataBase
) : ViewModel() {

    fun getFlow(query: String): Flow<PagingData<PokemonCardEntity>> {
        return getPager(query).flow.map { pagingData ->
            pagingData.map {
                it
            }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPager(query: String): Pager<Int, PokemonCardEntity> {
        return Pager(
            config = PagingConfig(10),
            remoteMediator = PokemonCardRemoteMediator(
                pokemonCardDb = pokemonCardsDb,
                query = query
            ),
            pagingSourceFactory = {
                pokemonCardsDb.dao.pagingSource()
            }
        )
    }

    fun testApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiRepository.search(page = 1, pageSize = 10, query = "pikachu")
            Log.d("TestAPI", "Response: $response")
        }
    }
}
