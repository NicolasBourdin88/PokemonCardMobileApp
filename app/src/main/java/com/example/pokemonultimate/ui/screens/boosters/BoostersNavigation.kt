package com.example.pokemonultimate.ui.screens.boosters

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokemonultimate.ui.navigation.OpeningBoostersNavigation
import com.example.pokemonultimate.ui.navigation.navigateToSelectedItem
import com.example.pokemonultimate.ui.screens.boosters.drawCard.DrawCardScreen

@Composable
fun BoostersNavigation(
    navController: NavHostController = rememberNavController(),
    currentController: (NavHostController) -> Unit,
    setFullScreen: () -> Unit,
) {
    currentController.invoke(navController)

    NavHost(
        navController = navController,
        startDestination = OpeningBoostersNavigation.startDestination,
    ) {
        composable<OpeningBoostersNavigation.BoosterDestination> {
            BoostersScreen(onBoosterOpened = {
                setFullScreen.invoke()
                navController.navigateToSelectedItem(
                    OpeningBoostersNavigation.DrawCardDestination(setId = "swsh1")
                )
            }
            )
        }
        composable<OpeningBoostersNavigation.DrawCardDestination> {
            val drawCardDestination: OpeningBoostersNavigation.DrawCardDestination = it.toRoute()
            DrawCardScreen(drawCardDestination.setId)
        }
        composable<OpeningBoostersNavigation.DisplayDrawnCardDestination> {}
    }
}
