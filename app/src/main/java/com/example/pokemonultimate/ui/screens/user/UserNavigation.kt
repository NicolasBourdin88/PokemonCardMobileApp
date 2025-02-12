package com.example.pokemonultimate.ui.screens.user

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.ui.navigation.UserNavigation
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel

@Composable
fun UserNavigation(
    navController: NavHostController = rememberNavController(),
    currentController: (NavHostController) -> Unit,
    displayBackAction: (Boolean) -> Unit,
    viewModel: AuthViewModel,
) {
    currentController.invoke(navController)

    NavHost(
        navController = navController,
        startDestination = UserNavigation.startDestination,
    ) {
        composable<UserNavigation.SetDestination> {
            displayBackAction.invoke(true)
            UserScreen(viewModel, navController)
        }
        composable<UserNavigation.ShowQrCodeDestination> {
            displayBackAction.invoke(true)
            ShowQRCodeScreen()
        }
        /*
        composable<UserNavigation.ScanCollectionDestination> {
            displayBackAction.invoke(true)
            ScanCollectionScreen()
        }
        composable<UserNavigation.DisplayScannedCardsDestination> {
            displayBackAction.invoke(true)
            val listImageUrl: UserNavigation.DisplayScannedCardsDestination = it.toRoute()
            DisplayScannedCardsScreen(listImageUrl.listImagePokemon)
        }*/
    }
}
