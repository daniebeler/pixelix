package com.daniebeler.pixelix.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class Navigate {
    fun navigate(route: String, navController: NavController) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateWithPopUp(route: String, navController: NavController) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
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
}