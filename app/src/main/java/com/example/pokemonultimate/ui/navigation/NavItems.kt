package com.example.pokemonultimate.ui.navigation

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.navigation.NavBackStackEntry
import com.example.pokemonultimate.R
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

    companion object {
        val startDestination = HomeDestination
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
