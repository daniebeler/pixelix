package com.daniebeler.pfpixelix.utils

import androidx.navigation.NavController

object Navigate {
    private var currentBottomBarRoute: String? = null
    private var restoreStateRoutes: List<String> = listOf("home_screen")

    fun changeAccount() {
        restoreStateRoutes = emptyList()
    }

    fun navigate(route: String, navController: NavController, singleTop: Boolean = true) {
        if (navController.currentDestination!!.route == route) {
            return
        }
        val alreadySaved = restoreStateRoutes.indexOf(route) != -1
        if (!alreadySaved) {
            restoreStateRoutes = restoreStateRoutes + route
        }
        navController.navigate(route) {
            launchSingleTop = singleTop
            restoreState = alreadySaved
        }
    }

    fun navigateWithPopUp(newRoute: String, navController: NavController) {
        if (navController.currentDestination!!.route == newRoute) {
            return
        }
        val alreadySaved = restoreStateRoutes.indexOf(newRoute) != -1
        if (!alreadySaved) {
            restoreStateRoutes = restoreStateRoutes + newRoute
        }
        if (newRoute == currentBottomBarRoute) {
            navController.navigate(newRoute) {
                popUpTo(currentBottomBarRoute!!)
                launchSingleTop = true
            }
        } else {
            navController.navigate(newRoute) {
                popUpTo(0) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = alreadySaved
            }
        }
        currentBottomBarRoute = newRoute
    }

    fun navigateAndDeleteBackStack(route: String, navController: NavController) {
        navController.navigate(route) {
            popUpTo(0) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }
}