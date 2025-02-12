package com.example.pokemonultimate.ui.screens.authentification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.data.utils.getUserId
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
