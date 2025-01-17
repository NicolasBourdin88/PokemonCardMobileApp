package com.example.pokemonultimate.ui.screens.inscription

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.ui.navigation.AuthenticationNavigation
import com.example.pokemonultimate.ui.screens.connection.ConnectionScreen
import com.example.pokemonultimate.ui.screens.connection.ConnectionViewModel


@Composable
fun AuthenticationNavigation(
    navController: NavHostController = rememberNavController(),
    inscriptionViewModel: InscriptionViewModel,
    connectionViewModel: ConnectionViewModel,
    onSuccess: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AuthenticationNavigation.startDestination,
    ) {
        composable<AuthenticationNavigation.SignInDestination> {
            ConnectionScreen(
                navController,
                viewModel = connectionViewModel,
                onSuccess = {
                    onSuccess()
                }
            )
        }
        composable<AuthenticationNavigation.SignUpDestination> {
            InscriptionScreen(
                navController,
                viewModel = inscriptionViewModel,
                onSuccess = {
                    onSuccess()
                }
            )
        }
    }
}
