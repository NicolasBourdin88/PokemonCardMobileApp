package com.example.pokemonultimate.ui.utils

fun String.setFirstToUpperCase() : String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}
