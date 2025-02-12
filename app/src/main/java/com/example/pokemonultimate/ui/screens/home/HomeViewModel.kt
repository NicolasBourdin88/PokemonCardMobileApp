package com.example.pokemonultimate.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.SetEntity
import com.example.pokemonultimate.data.model.pokemonCard.database.PokemonCardRemoteMediator
import com.example.pokemonultimate.data.model.sets.ImageSetEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val pokemonCardsDb: DataBase) : ViewModel() {

    private val _filters = MutableStateFlow<List<String>>(emptyList())

    init {
        syncCollection()
    }

    private fun syncCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiRepository.getSets()
            response.data?.let { sets ->
                pokemonCardsDb.setDao.insertSets(sets)
            }
        }
    }

    fun getFlow(
        query: String?,
        filtersTypes: List<String>,
        filtersSubTypes: List<String>,
        filtersSuperTypes: List<String>,
    ): Flow<PagingData<PokemonCardEntity>> {
        return getPager(
            query,
            filtersTypes,
            filtersSubTypes,
            filtersSuperTypes,
        ).flow.map { pagingData ->
            pagingData.map { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPager(
        query: String?,
        filtersTypes: List<String>,
        filtersSubTypes: List<String>,
        filtersSuperTypes: List<String>,
    ): Pager<Int, PokemonCardEntity> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = PokemonCardRemoteMediator(
                pokemonCardDb = pokemonCardsDb,
                query = query,
                filtersTypes = filtersTypes,
                filtersSubTypes = filtersSubTypes,
                filtersSuperTypes = filtersSuperTypes,
            ),
            pagingSourceFactory = {
                pokemonCardsDb.dao.pagingSource()
            }
        )
    }

    fun getFilters(filterCategories: FilterCategories) {
        val response = ApiRepository.getFilters(filterCategories)
        _filters.value = response.data ?: emptyList()
    }

    fun getFilteredFilters(searchValue: String): StateFlow<List<String>> {
        return _filters
            .map { filters ->
                if (searchValue.isEmpty()) {
                    filters
                } else {
                    filters.filter { it.contains(searchValue, ignoreCase = true) }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
    }
}
