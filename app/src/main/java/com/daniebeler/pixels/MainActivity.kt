package com.daniebeler.pixels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.daniebeler.pixels.ui.components.HomeComposable
import com.daniebeler.pixels.ui.components.LocalTimeline
import com.daniebeler.pixels.ui.components.LoggedInComposable
import com.daniebeler.pixels.ui.components.LoginComposable
import com.daniebeler.pixels.ui.components.ProfileComposable
import com.daniebeler.pixels.ui.components.SinglePostComposable
import com.daniebeler.pixels.ui.components.TrendingPostsComposable
import com.daniebeler.pixels.ui.theme.PixelsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getDailyTrendingPosts()
        mainViewModel.getMonthlyTrendingPosts()
        mainViewModel.getYearlyTrendingPosts()

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




sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object HomeScreen : Destinations(
        route = "home_screen",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    object Favourite : Destinations(
        route = "favourite_screen",
        title = "Favorite",
        icon = Icons.Outlined.Favorite
    )

    object Profile : Destinations(
        route = "profile_screen/{userid}",
        title = "Profile",
        icon = Icons.Outlined.Favorite
    )

    object SinglePost : Destinations(
        route = "single_post_screen/{postid}",
        title = "SinglePost",
        icon = Icons.Outlined.Favorite
    )

    object Notification : Destinations(
        route = "notification_screen",
        title = "Notification",
        icon = Icons.Outlined.Notifications
    )
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Destinations.HomeScreen.route) {
        composable(Destinations.HomeScreen.route) {
            HomeComposable(viewModel = viewModel, navController)
        }
        composable(Destinations.Favourite.route) {
            TrendingPostsComposable(viewModel = viewModel, navController)
        }
        composable(Destinations.Profile.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id->
                ProfileComposable(navController, userId = id)
            }

        }
        composable(Destinations.Notification.route) {
            LoginComposable(viewModel = viewModel, navController = navController)
        }
        composable(Destinations.SinglePost.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("postid")
            uId?.let { id->
                SinglePostComposable(navController, postId = id)
            }
        }

        composable(
            "fief",
            deepLinks = listOf(navDeepLink {
                uriPattern = "pixels-android-auth://callback?code={code}"
            }),
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            code?.let {
                LoggedInComposable(viewModel, navController, it)
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Destinations.HomeScreen, Destinations.Favourite, Destinations.Notification
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
