package com.example.pokemonultimate.ui.screens.inscription

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseAuth

class InscriptionViewModel : ViewModel() {

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

    private val _errorInscriptionMessage = mutableStateOf("");
    val errorInscriptionMessage: State<String> = _errorInscriptionMessage

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

    fun registerWithEmailAndPassword(email: String, password: String, confirmPassword: String, onResult: (Boolean) -> Unit) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            onErrorInscriptionMessageChanged("Tous les champs sont obligatoires.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onErrorInscriptionMessageChanged("Le format de l'email est incorrect.")
            return
        }

        if (password != confirmPassword) {
            onErrorInscriptionMessageChanged("Les mots de passe ne correspondent pas.")
            return
        }

        if (password.length < 6) {
            onErrorInscriptionMessageChanged("Le mot de passe doit contenir au moins 6 caractères.")
            return
        }

        _isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    onResult(true)
                } else {
                    onErrorInscriptionMessageChanged("Erreur d'inscription : ${task.exception?.message}")
                    onResult(false)
                }
            }
    }

    fun toggleBottomSheet() {
        _showBottomSheet.value = !_showBottomSheet.value
    }

    fun closeBottomSheet() {
        _showBottomSheet.value = false
    }


}
