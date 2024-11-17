package com.example.pokemonultimate.ui.screens.inscription

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.data.model.database.DataBase
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.data.utils.UserUtils.Companion.PROFILE_IMAGE_ID
import com.example.pokemonultimate.data.utils.UserUtils.Companion.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InscriptionViewModel @Inject constructor(private val pokemonCardsDb: DataBase) : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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

    private val _selectedImage = mutableStateOf<PokemonCellProfile?>(null)
    val selectedImage: State<PokemonCellProfile?> = _selectedImage

    fun onImageSelected(pokemonCellProfile: PokemonCellProfile) {
        _selectedImage.value = pokemonCellProfile
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

    fun registerWithEmailAndPassword(
        context: Context,
        onResult: (Boolean) -> Unit,
        onFail: (String) -> Unit
    ) {
        val email = _email.value
        val password = _password.value
        val confirmPassword = _confirmPassword.value
        val selectedImage = _selectedImage.value

        if (email.isEmpty() || password.isEmpty() || selectedImage == null) {
            onFail(context.getString(R.string.email_password_empty))
            onResult(false)
            return
        }

        if (password != confirmPassword) {
            onFail(context.getString(R.string.password_not_match))
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
                            onFail(context.getString(R.string.register_firestore))
                            onResult(false)
                        }
                    }
                }
            } else {
                onFail(context.getString(R.string.inscription_error) + task.exception?.message)
                onResult(false)
            }
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

    private fun saveUserProfileToFireStore(
        userId: String,
        picture: PokemonCellProfile,
        onComplete: (Boolean) -> Unit
    ) {
        val userDoc = hashMapOf(
            PROFILE_IMAGE_ID to picture.name
        )
        firestore.collection(USERS).document(userId).set(userDoc)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
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