package com.example.pokemonultimate.ui.screens.connection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ConnectionViewModel : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

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

    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            onErrorConnectionMessageChanged("Les champs email et mot de passe ne peuvent pas être vides.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onErrorConnectionMessageChanged("Le format de l'email est incorrect.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true)
                } else {
                    onErrorConnectionMessageChanged("Connexion échouée : ${task.exception?.message}")
                    onResult(false)
                }
            }
    }
/*
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }*/





}