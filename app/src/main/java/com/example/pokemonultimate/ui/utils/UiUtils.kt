package com.example.pokemonultimate.ui.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun String.setFirstToUpperCase(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

fun String.formatDate(): String {
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    val targetFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
    return sourceFormat.parse(this)?.let { targetFormat.format(it) } ?: ""
}