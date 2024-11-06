package com.example.pokemonultimate.ui.screens.inscription

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.ui.navigation.AuthenticationNavigation
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel
import com.example.pokemonultimate.ui.screens.connection.ConnectionScreen
import com.example.pokemonultimate.ui.screens.connection.ConnectionViewModel


@Composable
fun AuthenticationNavigation(
    navController: NavHostController = rememberNavController(),
    inscriptionViewModel: InscriptionViewModel,
    connectionViewModel: ConnectionViewModel,
    back: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AuthenticationNavigation.startDestination,
    ) {
        composable<AuthenticationNavigation.SignInDestination> {
            ConnectionScreen(navController, viewModel = connectionViewModel, onSuccess = {
                back.invoke()
            })
        }
        composable<AuthenticationNavigation.SignUpDestination> {
            InscriptionScreen(navController, viewModel = inscriptionViewModel, onSuccess = {
                back.invoke()
            })
        }
    }
}