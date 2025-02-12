package com.example.pokemonultimate.ui.screens.user

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokemonultimate.ui.navigation.UserNavigation
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel

@Composable
fun UserNavigation(
    navController: NavHostController = rememberNavController(),
    currentController: (NavHostController) -> Unit,
    displayBackAction: (Boolean) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = UserNavigation.startDestination,
    ) {
        composable<UserNavigation.SetDestination> {
            displayBackAction.invoke(true)
            val viewModel = hiltViewModel<AuthViewModel>()
            UserScreen(viewModel, navController)
        }
        composable<UserNavigation.ShowQrCodeDestination> {
            displayBackAction.invoke(true)
            currentController.invoke(navController)
            ShowQRCodeScreen()
        }

        composable<UserNavigation.ScanCollectionDestination> {
            displayBackAction.invoke(true)
            ScanCollectionScreen(navController)
        }
        composable<UserNavigation.DisplayScannedCardsDestination> {
            displayBackAction.invoke(true)
            val listImageUrl: UserNavigation.DisplayScannedCardsDestination = it.toRoute()
            DisplayScannedCardsScreen(listImageUrl.imageUrls)
        }
    }
}
