package com.example.pokemonultimate.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.ui.navigation.NavigationDestination.Companion.toDestination
import com.example.pokemonultimate.ui.screens.BoostersScreen
import com.example.pokemonultimate.ui.screens.CollectionScreen
import com.example.pokemonultimate.ui.screens.home.HomeScreen
import com.example.pokemonultimate.ui.screens.home.HomeViewModel

const val ICON_SIZE = 24

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.toDestination<MainNavigation>() ?: MainNavigation.startDestination
        }
    }

    val configuration = LocalConfiguration.current
    val isPortrait by remember { mutableStateOf(configuration.screenWidthDp < 500) }

    Scaffold(
        bottomBar = {
            if (isPortrait) BottomBarNavigation(currentDestination, navController)
        },
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(modifier = Modifier.padding(top = 54.dp, end = 16.dp), onClick = {

                }) {
                    Text("Se connecter")
                }
            }
        }
    ) { paddingValues ->
        Row(
            Modifier
                .fillMaxSize()
        ) {
            if (!isPortrait) RailBarNavigation(currentDestination, navController)

            NavHost(
                navController = navController,
                startDestination = MainNavigation.startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<MainNavigation.HomeDestination> {
                    val viewModel = hiltViewModel<HomeViewModel>()
                    HomeScreen(viewModel)
                }
                composable<MainNavigation.CollectionDestination> {
                    CollectionScreen()
                }
                composable<MainNavigation.BoostersDestination> {
                    BoostersScreen()
                }
            }
        }
    }
}

@Composable
private fun RailBarNavigation(
    currentDestination: MainNavigation?,
    navController: NavHostController
) {
    NavigationRail(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
        NavigationItem.entries.forEach { navItem ->
            val isSelected = currentDestination == navItem.destination

            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    navController.navigateToSelectedItem(navItem.destination)
                },
                icon = {
                    IconNavigation(isSelected, navItem)
                },
                label = {
                    Text(text = navItem.label)
                },
                colors = NavigationRailItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

@Composable
private fun BottomBarNavigation(
    currentDestination: MainNavigation?,
    navController: NavHostController
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
        NavigationItem.entries.forEach { navItem ->
            val isSelected = currentDestination == navItem.destination

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigateToSelectedItem(navItem.destination)
                },
                icon = {
                    IconNavigation(isSelected, navItem)
                },
                label = {
                    Text(text = navItem.label)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

@Composable
private fun IconNavigation(isSelected: Boolean, navigationItem: NavigationItem) {
    val resId = if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon

    Icon(
        painter = painterResource(id = resId),
        contentDescription = navigationItem.label,
        modifier = Modifier
            .height(ICON_SIZE.dp)
            .width(ICON_SIZE.dp)
    )
}

private fun NavHostController.navigateToSelectedItem(destination: MainNavigation) {
    navigate(destination) {
        popUpTo(this@navigateToSelectedItem.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when re-selecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}
