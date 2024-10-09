package com.example.pokemonultimate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemonultimate.data.model.pokemonCardModel.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCardModel.PokemonCardRemoteMediator
import com.example.pokemonultimate.data.model.pokemonCardModel.database.PokemonCardDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonCardsDb: PokemonCardDataBase
) : ViewModel() {

    fun getFlow(query: String?, filters: String?): Flow<PagingData<PokemonCardEntity>> {
        return getPager(query, filters).flow.map { pagingData ->
            pagingData.map {
                it
            }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPager(query: String?, filters: String?): Pager<Int, PokemonCardEntity> {
        return Pager(
            config = PagingConfig(10),
            remoteMediator = PokemonCardRemoteMediator(
                pokemonCardDb = pokemonCardsDb,
                query = query,
                filters = filters,
            ),
            pagingSourceFactory = {
                pokemonCardsDb.dao.pagingSource()
            }
        )
    }
}
