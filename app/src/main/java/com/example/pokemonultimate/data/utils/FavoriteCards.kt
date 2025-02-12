package com.example.pokemonultimate.data.utils

import com.example.pokemonultimate.data.model.pokemonCard.ImagePokemonEntity
import com.example.pokemonultimate.data.model.pokemonCard.ImagePokemonEntity.Companion.mapToImagePokemonEntity
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.data.model.pokemonCard.Rarity
import com.example.pokemonultimate.data.model.pokemonCard.SetEntity
import com.example.pokemonultimate.data.model.pokemonCard.SetEntity.Companion.mapToSetEntity
import com.example.pokemonultimate.data.model.pokemonCard.SubTypeEntity
import com.example.pokemonultimate.data.model.pokemonCard.TcgPlayerEntity.Companion.mapToTcgPlayerEntity
import com.example.pokemonultimate.data.model.pokemonCard.TypeEntity
import com.google.firebase.firestore.FirebaseFirestore

const val KEY_FIREBASE_COLLECTION = "drawnCards"

fun getUserCards(onFinish: (userCards: MutableSet<PokemonCardEntity>, documentExist: Boolean) -> Unit) {
    val userId = getUserId() ?: return
    val db = FirebaseFirestore.getInstance().collection("users").document(userId)

    db.get().addOnSuccessListener { document ->
        val userCards =
            document.get(KEY_FIREBASE_COLLECTION) as? List<Map<String, Any>> ?: emptyList()

        val cards = userCards.map { map ->
            PokemonCardEntity(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                supertype = (map["supertype"] as? String)?.let {
                    PokemonCardEntity.SuperType.valueOf(
                        it.uppercase()
                    )
                } ?: PokemonCardEntity.SuperType.POKEMON,
                subtypes = listOf(SubTypeEntity.EX),
                types = listOf(TypeEntity.FIRE),
                set = (map["set"] as? Map<String, Any>)?.let { mapToSetEntity(it) } ?: SetEntity(),
                number = map["number"] as? String ?: "",
                artist = map["artist"] as? String,
                rarity = (map["rarity"] as? String)?.let { Rarity.valueOf(it.uppercase()) }
                    ?: Rarity.COMMON,
                images = (map["images"] as? Map<String, Any>)?.let { mapToImagePokemonEntity(it) }
                    ?: ImagePokemonEntity(),
                tcgPlayer = (map["tcgplayer"] as? Map<String, Any>)?.let { mapToTcgPlayerEntity(it) },
            )
        }

        onFinish(cards.toMutableSet(), document.exists())
    }
}

fun getUserCardsFromId(userId: String, onFinish: (List<PokemonCardEntity>) -> Unit) {
    val db = FirebaseFirestore.getInstance().collection("users").document(userId)

    db.get().addOnSuccessListener { document ->
        val userCards =
            document.get(KEY_FIREBASE_COLLECTION) as? List<Map<String, Any>> ?: emptyList()

        val cards = userCards.map { map ->
            PokemonCardEntity(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                supertype = (map["supertype"] as? String)?.let {
                    PokemonCardEntity.SuperType.valueOf(it.uppercase())
                } ?: PokemonCardEntity.SuperType.POKEMON,
                subtypes = listOf(SubTypeEntity.EX),
                types = listOf(TypeEntity.FIRE),
                set = (map["set"] as? Map<String, Any>)?.let { mapToSetEntity(it) } ?: SetEntity(),
                number = map["number"] as? String ?: "",
                artist = map["artist"] as? String,
                rarity = (map["rarity"] as? String)?.let { Rarity.valueOf(it.uppercase()) }
                    ?: Rarity.COMMON,
                images = (map["images"] as? Map<String, Any>)?.let { mapToImagePokemonEntity(it) }
                    ?: ImagePokemonEntity(),
                tcgPlayer = (map["tcgplayer"] as? Map<String, Any>)?.let { mapToTcgPlayerEntity(it) },
            )
        }

        onFinish(cards)
    }
}

fun calculateUserStats(cards: Set<PokemonCardEntity>): Pair<Int, Int> {
    val totalCards = cards.size

    val completedSets = cards
        .filter { it.set.id.isNotEmpty() }
        .groupBy { it.set.id }
        .count { (_, cardsInSet) ->
            val setTotal = cardsInSet.first().set.total
            cardsInSet.size >= setTotal
        }

    return Pair(totalCards, completedSets)
}



