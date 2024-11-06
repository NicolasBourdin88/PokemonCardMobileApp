package com.example.pokemonultimate.ui.screens.connection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.data.model.PokemonCellProfil
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val pokemonCardsDb: DataBase
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isUserLoggedIn = mutableStateOf(false)
    val isUserLoggedIn: State<Boolean> = _isUserLoggedIn


    private val _errorConnectionMessage = mutableStateOf("");
    val errorConnectionMessage: State<String> = _errorConnectionMessage

    fun onEmailChangedChanged(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        _password.value = newValue
    }

    fun onErrorConnectionMessageChanged(newValue: String) {
        _errorConnectionMessage.value = newValue
    }


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signInWithEmailAndPassword(onResult: (Boolean) -> Unit) {
        val email = _email.value
        val password = _password.value

        if (email.isEmpty() || password.isEmpty()) {
            _errorConnectionMessage.value = "Les champs email et mot de passe ne peuvent pas être vides."
            onResult(false)
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Vérifie si l'utilisateur est déjà dans la base de données locale
                    checkUserInLocalDb(userId) { isUserInLocalDb ->
                        if (!isUserInLocalDb) {
                            // Si l'utilisateur n'est pas trouvé localement, récupère depuis Firestore
                            fetchUserFromFireStoreAndSaveLocally(userId) { success ->
                                onResult(success)
                            }
                        } else {
                            // L'utilisateur est déjà dans la base de données locale
                            onResult(true)
                        }
                    }
                }
            } else {
                _errorConnectionMessage.value = "Erreur de connexion : ${task.exception?.message}"
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

    private fun fetchUserFromFireStoreAndSaveLocally(userId: String, onComplete: (Boolean) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profileImageId = document.getString("profileImageId")
                    val profileImage = profileImageId?.let { PokemonCellProfil.valueOf(it) }
                    if (profileImage != null) {
                        saveUserProfileLocally(userId, profileImage) {
                            onComplete(true)
                        }
                    } else {
                        _errorConnectionMessage.value = "Erreur : L'image de profil n'est pas valide."
                        onComplete(false)
                    }
                } else {
                    _errorConnectionMessage.value = "Erreur : Profil utilisateur non trouvé dans Firestore."
                    onComplete(false)
                }
            }
            .addOnFailureListener { e ->
                _errorConnectionMessage.value = "Erreur lors de la récupération depuis Firestore : ${e.message}"
                onComplete(false)
            }
    }


    private fun saveUserProfileLocally(userId: String, imageId: PokemonCellProfil, onComplete: () -> Unit) {
        viewModelScope.launch {
            val userProfile = UserProfile(userId, imageId)
            pokemonCardsDb.userProfileDao.insertUserProfile(userProfile)
            onComplete()
        }
    }
}