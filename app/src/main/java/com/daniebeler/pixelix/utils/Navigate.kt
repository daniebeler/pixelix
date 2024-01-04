package com.daniebeler.pixelix.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class Navigate {
    fun navigate(route: String, navController: NavController) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }
}