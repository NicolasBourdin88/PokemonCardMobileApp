package com.example.pokemonultimate.data.utils

import com.google.firebase.auth.FirebaseAuth

fun getUserId() : String? {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    return userId
}