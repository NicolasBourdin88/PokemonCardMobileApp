package com.example.pokemonultimate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen() {
    Column {
        Text("Home screens")
        Button(
            onClick = {
                println("click")
            },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
        ) {
            Text("test")
        }
    }
}
