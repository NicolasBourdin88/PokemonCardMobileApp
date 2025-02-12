package com.example.pokemonultimate.ui.screens.authentification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.utils.getUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val pokemonCardsDb: DataBase) : ViewModel() {

    val isUserLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userProfileImage: MutableStateFlow<PokemonCellProfile?> = MutableStateFlow(null)

    init {
        checkUserLoggedIn()
    }

    fun checkUserLoggedIn() {
        getUserId()?.let {
            isUserLoggedIn.value = true
            loadUserProfile(it)
        } ?: {
            isUserLoggedIn.value = false
        }
    }

    private fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            val userProfile = pokemonCardsDb.userProfileDao.getUserProfile(userId)
            userProfile?.let { userProfileImage.value = it.pokemonCell }
        }
    }
}
