package com.example.pokemonultimate.ui.screens.authentification

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import com.example.pokemonultimate.data.model.PokemonCellProfil
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.utils.getUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pokemonCardsDb: DataBase
) : ViewModel() {

    val isUserLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val userProfileImage: MutableStateFlow<PokemonCellProfil?> = MutableStateFlow(null)

    init {
        checkUserLoggedIn()
    }

    fun checkUserLoggedIn() {
        getUserId()?.let {
            isUserLoggedIn.value = true
            loadUserProfile(it)
            viewModelScope.launch {
                val allProfiles = pokemonCardsDb.userProfileDao.getAllProfiles()
                println("test1011 All profiles in DB: $allProfiles")
            }
        } ?: {
            isUserLoggedIn.value = false
        }
    }

    private fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            val userProfile = pokemonCardsDb.userProfileDao.getUserProfile(userId)
            println("test1011 userprofil : "+userProfile);
            userProfile?.let {
                println("test1011 AuthViewModel: User profile loaded: ${it.pokemonCell},${it.pokemonCell.pokemonCellImage},${it.pokemonCell.name},${it.pokemonCell.ordinal}")
                userProfileImage.value = it.pokemonCell
            }
        }
    }
}