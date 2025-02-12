package com.example.pokemonultimate.ui.screens.boosters

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokemonultimate.ui.navigation.OpeningBoostersNavigation
import com.example.pokemonultimate.ui.navigation.navigateToSelectedItem
import com.example.pokemonultimate.ui.screens.boosters.drawCard.DrawCardScreen
import com.example.pokemonultimate.ui.screens.boosters.drawCard.DrawCardViewModel

@Composable
fun BoostersNavigation(
    navController: NavHostController = rememberNavController(),
    currentController: (NavHostController) -> Unit,
    setFullScreen: (Boolean) -> Unit,
) {
    currentController.invoke(navController)

    val drawCardViewModel: DrawCardViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = OpeningBoostersNavigation.BoosterDestination,
    ) {
        composable<OpeningBoostersNavigation.BoosterDestination> {
            setFullScreen.invoke(false)
            BoostersScreen(drawCardViewModel.displaySetImage.value, onBoosterOpened = {
                navController.navigateToSelectedItem(
                    OpeningBoostersNavigation.DrawCardDestination(setId = drawCardViewModel.displaySet.value)
                )
            })
        }
        composable<OpeningBoostersNavigation.DrawCardDestination> {
            setFullScreen.invoke(true)
            val drawCardDestination: OpeningBoostersNavigation.DrawCardDestination = it.toRoute()
            DrawCardScreen(drawCardDestination.setId, drawCardViewModel, onFinish = {
                drawCardViewModel.setSet()
                navController.navigate(OpeningBoostersNavigation.BoosterDestination)
            })
        }
    }
}
