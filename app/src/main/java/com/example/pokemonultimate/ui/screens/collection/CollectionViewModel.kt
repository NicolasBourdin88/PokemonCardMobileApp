package com.example.pokemonultimate.ui.screens.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.api.ApiRepository
import com.example.pokemonultimate.data.api.ApiResponse
import com.example.pokemonultimate.data.model.sets.Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {

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
}
