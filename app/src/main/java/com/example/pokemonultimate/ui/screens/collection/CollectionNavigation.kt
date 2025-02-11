package com.example.pokemonultimate.ui.screens.collection

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokemonultimate.ui.navigation.CollectionNavigation
import com.example.pokemonultimate.ui.screens.card.CardScreen
import com.example.pokemonultimate.ui.screens.card.CardViewModel
import com.example.pokemonultimate.ui.screens.collection.cardList.CardListScreen
import com.example.pokemonultimate.ui.screens.collection.set.ListSetScreen

@Composable
fun CollectionNavigation(
    navController: NavHostController = rememberNavController(),
    collectionViewModel: CollectionViewModel,
    currentController: (NavHostController) -> Unit,
    displayBackAction: (Boolean) -> Unit,
) {
    currentController.invoke(navController)

    NavHost(
        navController = navController,
        startDestination = CollectionNavigation.startDestination,
    ) {
        composable<CollectionNavigation.SetDestination> {
            displayBackAction.invoke(false)
            ListSetScreen(navController, collectionViewModel = collectionViewModel)
        }
        composable<CollectionNavigation.CardListDestination> {
            displayBackAction.invoke(true)
            val cardListDestination: CollectionNavigation.CardListDestination = it.toRoute()
            CardListScreen(
                cardListDestination.setId,
                cardListDestination.setImage,
                cardListDestination.cardFromHome,
                collectionViewModel = collectionViewModel,
                navController = navController
            )
        }
        composable<CollectionNavigation.CardDestination> {
            val cardDestination: CollectionNavigation.CardDestination = it.toRoute()
            displayBackAction.invoke(true)
            CardScreen(cardDestination.jsonCard, cardDestination.withButtonCollection, navController)
        }
    }
}
