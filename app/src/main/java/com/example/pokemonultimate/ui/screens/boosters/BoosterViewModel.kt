package com.example.pokemonultimate.ui.screens.boosters

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.utils.NetworkAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BoosterViewModel @Inject constructor(
    application: Application,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val isNetworkAvailable = NetworkAvailability(application).isNetworkAvailable
        .mapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )
}
