package com.example.pokemonultimate.ui.screens.inscription

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
class InscriptionViewModel @Inject constructor(
    private val pokemonCardsDb: DataBase
) : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> = _confirmPassword

    private val _showBottomSheet = mutableStateOf(false)
    val showBottomSheet: State<Boolean> = _showBottomSheet

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _errorInscriptionMessage = mutableStateOf("")
    val errorInscriptionMessage: State<String> = _errorInscriptionMessage

    private val _selectedImage = mutableStateOf<PokemonCellProfil?>(null)
    val selectedImage: State<PokemonCellProfil?> = _selectedImage

    private val _isUserLoggedIn = mutableStateOf(false)
    val isUserLoggedIn: State<Boolean> = _isUserLoggedIn

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun onImageSelected(pokemonCellProfil: PokemonCellProfil) {
        _selectedImage.value = pokemonCellProfil
    }

    fun onEmailChanged(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        _password.value = newValue
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _confirmPassword.value = newValue
    }

    fun onErrorInscriptionMessageChanged(newValue: String) {
        _errorInscriptionMessage.value = newValue
    }

    fun registerWithEmailAndPassword(onResult: (Boolean) -> Unit) {
        val email = _email.value
        val password = _password.value
        val confirmPassword = _confirmPassword.value
        val selectedImage = _selectedImage.value

        if (email.isEmpty() || password.isEmpty() || selectedImage == null) {
            _errorInscriptionMessage.value = "Tous les champs doivent être remplis"
            onResult(false)
            return
        }

        if (password != confirmPassword) {
            _errorInscriptionMessage.value = "Les mots de passe ne correspondent pas"
            onResult(false)
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    saveUserProfileToFireStore(userId, selectedImage) { firestoreSuccess ->
                        if (firestoreSuccess) {
                            saveUserProfileLocally(userId, selectedImage) {
                                onResult(true)
                            }
                        } else {
                            _errorInscriptionMessage.value = "Erreur d'enregistrement dans Firestore"
                            onResult(false)
                        }
                    }
                }
            } else {
                _errorInscriptionMessage.value = "Erreur d'inscription: ${task.exception?.message}"
                onResult(false)
            }
        }
    }


    private fun saveUserProfileLocally(userId: String, imageId: PokemonCellProfil, onComplete: () -> Unit) {
        viewModelScope.launch {
                val userProfile = UserProfile(userId, imageId)
                pokemonCardsDb.userProfileDao.insertUserProfile(userProfile)
                onComplete()
        }
    }

    private fun saveUserProfileToFireStore(userId: String, picture: PokemonCellProfil,  onComplete: (Boolean) -> Unit) {
        val userDoc = hashMapOf(
            "profileImageId" to picture.name
        )
        firestore.collection("users").document(userId).set(userDoc)
            .addOnSuccessListener {
                println("Utilisateur enregistré avec l'ID de l'image de profil dans Firestore")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Erreur lors de l'enregistrement de l'utilisateur : ${e.message}")
                onComplete(false)
            }
    }


    fun toggleBottomSheet() {
        _showBottomSheet.value = !_showBottomSheet.value
    }

    fun closeBottomSheet() {
        _showBottomSheet.value = false
    }


}
