package com.example.pokemonultimate.ui.screens.authentification

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.utils.getUserId
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val pokemonCardsDb: DataBase,
    @ApplicationContext private val context: Context
) : ViewModel() {

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

    fun logoutUser() {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            pokemonCardsDb.userProfileDao.clearUserData()
            clearUserId()

            isUserLoggedIn.value = false
            userProfileImage.value = null
        }
    }

    private fun clearUserId() {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("user_id").apply()
    }


}
