package com.daniebeler.pixels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.daniebeler.pixels.ui.components.HomeComposable
import com.daniebeler.pixels.ui.components.OwnProfileComposable
import com.daniebeler.pixels.ui.components.ProfileComposable
import com.daniebeler.pixels.ui.components.SettingsComposable
import com.daniebeler.pixels.ui.components.SinglePostComposable
import com.daniebeler.pixels.ui.components.TrendingComposable
import com.daniebeler.pixels.ui.components.TrendingPostsComposable
import com.daniebeler.pixels.ui.theme.PixelsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Default).launch {
            val accessToken: String = mainViewModel.getAccessTokenFromStorage().first()

            if (accessToken.isEmpty()) {
                gotoLoginActivity(this@MainActivity)
            }
        }

        mainViewModel.getDailyTrendingPosts()
        mainViewModel.getMonthlyTrendingPosts()
        mainViewModel.getYearlyTrendingPosts()
        mainViewModel.getTrendingHashtags()



        setContent {
            PixelsTheme {
                val navController: NavHostController = rememberNavController()

                var buttonsVisible = remember { mutableStateOf(true) }

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            state = buttonsVisible
                        )
                    }) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavigationGraph(navController = navController, viewModel = mainViewModel)
                    }
                }
            }
        }
    }
}

fun gotoLoginActivity(context: Context){
    val intent = Intent(context, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
}


sealed class Destinations(
    val route: String,
    val icon: ImageVector? = null
) {
    object HomeScreen : Destinations(
        route = "home_screen",
        icon = Icons.Outlined.Home
    )

    object TrendingScreen : Destinations(
        route = "trending_screen",
        icon = Icons.AutoMirrored.Outlined.TrendingUp
    )

    object OwnProfile : Destinations(
        route = "own_profile_screen",
        icon = Icons.Outlined.AccountCircle
    )

    object Profile : Destinations(
        route = "profile_screen/{userid}",
        icon = Icons.Outlined.Favorite
    )

    object Settings : Destinations(
        route = "settings_screen",
        icon = Icons.Outlined.Settings
    )

    object SinglePost : Destinations(
        route = "single_post_screen/{postid}",
        icon = Icons.Outlined.Favorite
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Destinations.HomeScreen.route) {
        composable(Destinations.HomeScreen.route) {
            HomeComposable(viewModel = viewModel, navController)
        }
        composable(Destinations.TrendingScreen.route) {
            TrendingComposable(viewModel = viewModel, navController)
        }
        composable(Destinations.Profile.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id->
                ProfileComposable(navController, userId = id)
            }

        }

        composable(Destinations.Settings.route) {
            SettingsComposable(navController, viewModel)
        }
        composable(Destinations.OwnProfile.route) {
            OwnProfileComposable(viewModel, navController)
        }
        composable(Destinations.SinglePost.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("postid")
            uId?.let { id->
                SinglePostComposable(navController, postId = id)
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.TrendingScreen, Destinations.OwnProfile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                icon = {
                    Icon(imageVector = screen.icon!!, contentDescription = "")
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
