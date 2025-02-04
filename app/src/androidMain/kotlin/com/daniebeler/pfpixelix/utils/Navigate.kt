package com.daniebeler.pfpixelix.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

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
                popUpTo(navController.graph.findStartDestination().id) {
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

    fun openUrlInApp(context: Context, url: String) {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(context, Uri.parse(url))
    }

    fun openUrlInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}