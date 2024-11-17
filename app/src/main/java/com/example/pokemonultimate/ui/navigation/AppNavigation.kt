package com.example.pokemonultimate.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pokemonultimate.data.utils.getUserId
import com.example.pokemonultimate.ui.navigation.NavigationDestination.Companion.toDestination
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel
import com.example.pokemonultimate.ui.screens.boosters.BoostersScreen
import com.example.pokemonultimate.ui.screens.collection.CollectionNavigation
import com.example.pokemonultimate.ui.screens.collection.CollectionViewModel
import com.example.pokemonultimate.ui.screens.connection.ConnectionViewModel
import com.example.pokemonultimate.ui.screens.home.HomeScreen
import com.example.pokemonultimate.ui.screens.home.HomeViewModel
import com.example.pokemonultimate.ui.screens.inscription.AuthenticationNavigation
import com.example.pokemonultimate.ui.screens.inscription.InscriptionViewModel
import com.example.pokemonultimate.ui.screens.user.UserScreen
import com.example.pokemonultimate.ui.utils.Padding

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
    val shouldDisplayBackAction = remember { mutableStateOf(false) }
    val currentBackNavController = remember { mutableStateOf(navController) }

    val authViewModel: AuthViewModel = viewModel()
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()
    val userProfileImageId by authViewModel.userProfileImage.collectAsState()
    val isInFullScreen = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (isPortrait && !isOnUserScreen(navBackStackEntry) && !isInFullScreen.value) BottomBarNavigation(currentDestination, navController)
        },
        topBar = {
            if (!isInFullScreen.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Padding.MINI.dp,
                            end = Padding.MINI.dp,
                            top = Padding.GIANT.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (shouldDisplayBackAction.value) {
                        IconButton(onClick = {
                            if (currentBackNavController.value.previousBackStackEntry != null) {
                                currentBackNavController.value.navigateUp()
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
                if (!isOnUserScreen(navBackStackEntry)) {
                    getUserId().let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (!isUserLoggedIn) {
                                Button(
                                    modifier = Modifier.padding(top = Padding.MASSIVE.dp, end = Padding.MEDIUM.dp),
                                    onClick = {
                                        navController.navigate(MainNavigation.AuthenticationDestination)
                                    }
                                ) {
                                    Text("Sign in")
                                }
                            } else {
                                userProfileImageId?.let { profile ->
                                    Box(
                                        modifier = Modifier
                                            .padding(top = Padding.MASSIVE.dp, end = Padding.MEDIUM.dp)
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(profile.brush)
                                            .clickable {
                                                navController.navigate(MainNavigation.UserDestination)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = profile.pokemonCellImage),
                                            contentDescription = "Profile",
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(
            Modifier
                .fillMaxSize()
        ) {
            if (!isPortrait && !isOnUserScreen(navBackStackEntry) && !isInFullScreen.value) RailBarNavigation(currentDestination, navController)

            NavHost(
                navController = navController,
                startDestination = MainNavigation.startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<MainNavigation.HomeDestination> {
                    shouldDisplayBackAction.value = false
                    val viewModel = hiltViewModel<HomeViewModel>()
                    HomeScreen(viewModel)
                }
                composable<MainNavigation.CollectionDestination> {
                    val viewModel = hiltViewModel<CollectionViewModel>()
                    shouldDisplayBackAction.value = false
                    CollectionNavigation(
                        collectionViewModel = viewModel,
                        currentController = {
                            currentBackNavController.value = it
                        },
                        displayBackAction = {
                            shouldDisplayBackAction.value = it
                        })
                }
                composable<MainNavigation.BoostersDestination> {
                    shouldDisplayBackAction.value = false
                    BoostersScreen(onBoosterOpened = {
                        isInFullScreen.value = true
                    })
                }
                composable<MainNavigation.AuthenticationDestination> {
                    val inscriptionViewModel = hiltViewModel<InscriptionViewModel>()
                    val connectionViewModel = hiltViewModel<ConnectionViewModel>()

                    shouldDisplayBackAction.value = true
                    AuthenticationNavigation(
                        inscriptionViewModel = inscriptionViewModel,
                        connectionViewModel = connectionViewModel,
                        onSuccess = {
                            navController.popBackStack()
                            authViewModel.checkUserLoggedIn()
                        }
                    )
                }
                composable<MainNavigation.UserDestination> {
                    UserScreen()
                }
            }
        }
    }
}

@Composable
private fun isOnUserScreen(navBackStackEntry: NavBackStackEntry?): Boolean {
    val currentDestination = navBackStackEntry?.toDestination<MainNavigation>()

    return when (currentDestination) {
        is MainNavigation.UserDestination, is MainNavigation.AuthenticationDestination -> true
        else -> false
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

fun NavHostController.navigateToSelectedItem(destination: NavigationDestination) {
    navigate(destination) {
        popUpTo(this@navigateToSelectedItem.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true

        if (destination != MainNavigation.CollectionDestination) {
            restoreState = true
        }
    }
}