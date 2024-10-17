package com.example.pokemonultimate.ui.screens.collection

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.ui.navigation.CollectionNavigation
import com.example.pokemonultimate.ui.screens.collection.set.ListSetScreen

@Composable
fun CollectionNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = CollectionNavigation.startDestination,
    ) {
        composable<CollectionNavigation.SetDestination> {
            ListSetScreen(navController)
        }
        composable<CollectionNavigation.CardListDestination> {
            Text("List card")
        }
        composable<CollectionNavigation.CardDestination> {
            Text("Card")
        }
    }
}
