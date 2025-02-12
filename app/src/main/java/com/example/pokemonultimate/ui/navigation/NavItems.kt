package com.example.pokemonultimate.ui.navigation

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Serializable
sealed class MainNavigation : NavigationDestination() {

    @Serializable
    data object HomeDestination : MainNavigation()

    @Serializable
    data object CollectionDestination : MainNavigation()

    @Serializable
    data object BoostersDestination : MainNavigation()

    @Serializable
    data object UserDestination : MainNavigation()

    @Serializable
    data object AuthenticationDestination : MainNavigation()

    companion object {
        val startDestination = HomeDestination
    }
}

@Serializable
sealed class AuthenticationNavigation : NavigationDestination() {

    @Serializable
    data object SignInDestination : AuthenticationNavigation()

    @Serializable
    data object SignUpDestination : AuthenticationNavigation()

    companion object {
        val startDestination = SignInDestination
    }
}

@Serializable
sealed class UserNavigation : NavigationDestination() {

    @Serializable
    data object SetDestination : UserNavigation()

    @Serializable
    data object ShowQrCodeDestination : UserNavigation()

    @Serializable
    data object ScanCollectionDestination : UserNavigation()

    @Serializable
    data object DisplayScannedCardsDestination : UserNavigation()

    companion object {
        val startDestination = SetDestination
    }
}

@Serializable
sealed class CollectionNavigation : NavigationDestination() {

    @Serializable
    data object SetDestination : CollectionNavigation()

    @Serializable
    data class CardListDestination(val setImage: String, val setId: String) : CollectionNavigation()

    @Serializable
    data object CardDestination : CollectionNavigation()

    companion object {
        val startDestination = SetDestination
    }
}

@Serializable
sealed class OpeningBoostersNavigation : NavigationDestination() {

    @Serializable
    data object BoosterDestination : OpeningBoostersNavigation()

    @Serializable
    data class DrawCardDestination(val setId: String) : OpeningBoostersNavigation()

    companion object {
        val startDestination = BoosterDestination
    }
}

enum class NavigationItem(
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val destination: MainNavigation,
) {
    COLLECTION(
        label = "Collection",
        selectedIcon = R.drawable.ic_catalog_selected,
        unselectedIcon = R.drawable.ic_catalog_unselected,
        destination = MainNavigation.CollectionDestination
    ),
    HOME(
        label = "Home",
        selectedIcon = R.drawable.ic_home_selected,
        unselectedIcon = R.drawable.ic_home_unfilled,
        destination = MainNavigation.HomeDestination
    ),
    BOOSTERS(
        label = "Boosters",
        selectedIcon = R.drawable.ic_boosters_selected,
        unselectedIcon = R.drawable.ic_boosters_unselected,
        destination = MainNavigation.BoostersDestination
    ),

}

@Serializable
sealed class NavigationDestination {
    companion object {
        inline fun <reified T : NavigationDestination> NavBackStackEntry.toDestination(): T? {
            return toDestination(T::class, backStackEntry = this)
        }

        fun <T : NavigationDestination> toDestination(
            kClass: KClass<T>,
            backStackEntry: NavBackStackEntry?
        ): T? {

            fun kClassFromRoute(route: String) = kClass.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }

            if (backStackEntry == null) return null

            val route = backStackEntry.destination.route ?: ""
            val args = backStackEntry.arguments
            val subclass = kClassFromRoute(route) ?: return null

            return createInstance(subclass, args)
        }

        private fun <T : NavigationDestination> createInstance(
            kClass: KClass<T>,
            bundle: Bundle?
        ): T? {
            return kClass.primaryConstructor?.let {
                val args = it.parameters.associateWith { parameter -> bundle?.get(parameter.name) }
                it.callBy(args)
            } ?: kClass.objectInstance
        }
    }
}