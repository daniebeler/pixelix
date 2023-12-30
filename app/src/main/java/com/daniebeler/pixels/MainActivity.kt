package com.daniebeler.pixels

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.outlined.Mail
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daniebeler.pixels.ui.composables.timelines.hashtag_timeline.HashtagTimelineComposable
import com.daniebeler.pixels.ui.composables.HomeComposable
import com.daniebeler.pixels.ui.composables.settings.muted_accounts.MutedAccountsComposable
import com.daniebeler.pixels.ui.composables.NotificationsComposable
import com.daniebeler.pixels.ui.composables.OwnProfileComposable
import com.daniebeler.pixels.ui.composables.ProfileComposable
import com.daniebeler.pixels.ui.composables.settings.SettingsComposable
import com.daniebeler.pixels.ui.composables.SinglePostComposable
import com.daniebeler.pixels.ui.composables.followers.FollowersMainComposable
import com.daniebeler.pixels.ui.composables.settings.blocked_accounts.BlockedAccountsComposable
import com.daniebeler.pixels.ui.composables.settings.bookmarked_posts.BookmarkedPostsComposable
import com.daniebeler.pixels.ui.composables.settings.followed_hashtags.FollowedHashtagsComposable
import com.daniebeler.pixels.ui.composables.settings.liked_posts.LikedPostsComposable
import com.daniebeler.pixels.ui.composables.trending.TrendingComposable
import com.daniebeler.pixels.ui.theme.PixelsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

     private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!mainViewModel.doesTokenExist()) {
            gotoLoginActivity(this@MainActivity)
        } else {
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

    object NotificationsScreen : Destinations(
        route = "notifications_screen",
        icon = Icons.Outlined.Mail
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

    object MutedAccounts : Destinations(
        route = "muted_accounts_screen",
        icon = Icons.Outlined.Settings
    )

    object BlockedAccounts : Destinations(
        route = "blocked_accounts_screen",
        icon = Icons.Outlined.Settings
    )

    object LikedPosts : Destinations(
        route = "liked_posts_screen",
        icon = Icons.Outlined.Settings
    )

    object BookmarkedPosts : Destinations(
        route = "bookmarked_posts_screen",
        icon = Icons.Outlined.Settings
    )

    object FollowedHashtags : Destinations(
        route = "followed_hashtags_screen",
        icon = Icons.Outlined.Settings
    )

    object Hashtag : Destinations(
        route = "hashtag_timeline_screen/{hashtag}",
        icon = Icons.Outlined.Settings
    )

    object SinglePost : Destinations(
        route = "single_post_screen/{postid}",
        icon = Icons.Outlined.Favorite
    )

    object Followers : Destinations(
        route = "followers_screen/{userid}",
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
            TrendingComposable(navController)
        }

        composable(Destinations.NotificationsScreen.route) {
            NotificationsComposable(navController)
        }

        composable(Destinations.Profile.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id->
                ProfileComposable(navController, userId = id)
            }
        }

        composable(Destinations.Hashtag.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("hashtag")
            uId?.let { id->
                HashtagTimelineComposable(navController, id)
            }
        }

        composable(Destinations.Settings.route) {
            SettingsComposable(navController, viewModel)
        }

        composable(Destinations.MutedAccounts.route) {
            MutedAccountsComposable(navController)
        }

        composable(Destinations.BlockedAccounts.route) {
            BlockedAccountsComposable(navController)
        }

        composable(Destinations.LikedPosts.route) {
            LikedPostsComposable(navController)
        }

        composable(Destinations.BookmarkedPosts.route) {
            BookmarkedPostsComposable(navController)
        }

        composable(Destinations.FollowedHashtags.route) {
            FollowedHashtagsComposable(navController)
        }

        composable(Destinations.OwnProfile.route) {
            OwnProfileComposable(navController)
        }

        composable(Destinations.Followers.route) {navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id ->
                FollowersMainComposable(navController, accountId = id)
            }
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
        Destinations.HomeScreen, Destinations.TrendingScreen, Destinations.NotificationsScreen, Destinations.OwnProfile
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
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
