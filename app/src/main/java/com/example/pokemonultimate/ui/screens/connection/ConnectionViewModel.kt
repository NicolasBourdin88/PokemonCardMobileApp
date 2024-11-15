package com.example.pokemonultimate.ui.screens.connection

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.data.utils.CommonConstants.Companion.PROFILE_IMAGE_ID
import com.example.pokemonultimate.data.utils.CommonConstants.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val pokemonCardsDb: DataBase
) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _errorConnectionMessage = mutableStateOf("")
    val errorConnectionMessage: State<String> = _errorConnectionMessage

    fun onEmailChangedChanged(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        _password.value = newValue
    }

    fun signInWithEmailAndPassword(
        context: Context,
        onResult: (Boolean) -> Unit,
        onFail: (String) -> Unit
    ) {
        val email = _email.value
        val password = _password.value

        if (email.isEmpty() || password.isEmpty()) {
            onFail(context.getString(R.string.email_password_empty))
            onResult(false)
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    checkUserInLocalDb(userId) { isUserInLocalDb ->
                        if (!isUserInLocalDb) {
                            fetchUserFromFireStoreAndSaveLocally(
                                userId = userId,
                                context = context,
                                onComplete = { success ->
                                    if (success) {
                                        onResult(true)
                                    } else {
                                        onResult(false)
                                    }
                                }
                            )
                        } else {
                            onResult(true)
                        }
                    }
                }
            } else {
                onFail(context.getString(R.string.connection_error) + task.exception?.message)
                onResult(false)
            }
        }
    }

    private fun checkUserInLocalDb(userId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = pokemonCardsDb.userProfileDao.getUserProfile(userId)
            onComplete(user != null)
        }
    }

    private fun fetchUserFromFireStoreAndSaveLocally(
        context: Context,
        userId: String,
        onComplete: (Boolean) -> Unit
    ) {
        firestore.collection(USERS).document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profileImageId = document.getString(PROFILE_IMAGE_ID)
                    val profileImage = profileImageId?.let { PokemonCellProfile.valueOf(it) }
                    if (profileImage != null) {
                        saveUserProfileLocally(userId, profileImage) {
                            onComplete(true)
                        }
                    } else {
                        _errorConnectionMessage.value =
                            context.getString(R.string.picture_not_valid)
                        onComplete(false)
                    }
                } else {
                    _errorConnectionMessage.value =
                        context.getString(R.string.user_not_found_firestore)
                    onComplete(false)
                }
            }
            .addOnFailureListener {
                _errorConnectionMessage.value = context.getString(R.string.error_fetch_firestore)
                onComplete(false)
            }
    }

    private fun saveUserProfileLocally(
        userId: String,
        imageId: PokemonCellProfile,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            val userProfile = UserProfile(userId, imageId)
            pokemonCardsDb.userProfileDao.insertUserProfile(userProfile)
            onComplete()
        }
    }

}