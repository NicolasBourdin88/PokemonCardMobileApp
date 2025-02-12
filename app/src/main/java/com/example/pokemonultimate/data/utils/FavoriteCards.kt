package com.example.pokemonultimate.data.utils

import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.google.firebase.firestore.DocumentReference

const val KEY_FIREBASE_COLLECTION = "drawnCards"

fun getUserCards(
    userDocument: DocumentReference,
    onFinish: (userCards: MutableSet<PokemonCardEntity>, documentExist: Boolean) -> Unit
) {
    userDocument.get().addOnSuccessListener { document ->
        val userCards = runCatching {
            document.get(KEY_FIREBASE_COLLECTION) as? MutableList<PokemonCardEntity>
        }.getOrNull() ?: mutableListOf()
        onFinish.invoke(userCards.toMutableSet(), document.exists())
    }
}
