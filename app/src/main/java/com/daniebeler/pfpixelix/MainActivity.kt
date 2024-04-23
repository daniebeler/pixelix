package com.daniebeler.pfpixelix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.ui.composables.HomeComposable
import com.daniebeler.pfpixelix.ui.composables.collection.CollectionComposable
import com.daniebeler.pfpixelix.ui.composables.direct_messages.chat.ChatComposable
import com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations.ConversationsComposable
import com.daniebeler.pfpixelix.ui.composables.edit_post.EditPostComposable
import com.daniebeler.pfpixelix.ui.composables.edit_profile.EditProfileComposable
import com.daniebeler.pfpixelix.ui.composables.followers.FollowersMainComposable
import com.daniebeler.pfpixelix.ui.composables.newpost.NewPostComposable
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsComposable
import com.daniebeler.pfpixelix.ui.composables.profile.other_profile.OtherProfileComposable
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.OwnProfileComposable
import com.daniebeler.pfpixelix.ui.composables.search.SearchComposable
import com.daniebeler.pfpixelix.ui.composables.settings.about_instance.AboutInstanceComposable
import com.daniebeler.pfpixelix.ui.composables.settings.about_pixelix.AboutPixelixComposable
import com.daniebeler.pfpixelix.ui.composables.settings.blocked_accounts.BlockedAccountsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.bookmarked_posts.BookmarkedPostsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.followed_hashtags.FollowedHashtagsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.liked_posts.LikedPostsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.muted_accounts.MutedAccountsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.PreferencesComposable
import com.daniebeler.pfpixelix.ui.composables.single_post.SinglePostComposable
import com.daniebeler.pfpixelix.ui.composables.timelines.hashtag_timeline.HashtagTimelineComposable
import com.daniebeler.pfpixelix.ui.composables.trending.TrendingComposable
import com.daniebeler.pfpixelix.ui.theme.PixelixTheme
import com.daniebeler.pfpixelix.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: CountryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (!repository.doesAccessTokenExist()) {
            gotoLoginActivity(this@MainActivity)
        } else {
            setContent {
                PixelixTheme {
                    val navController: NavHostController = rememberNavController()

                    Scaffold(bottomBar = {
                        BottomBar(navController = navController)
                    }) { paddingValues ->
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            NavigationGraph(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }


    }
}

fun gotoLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
}


sealed class Destinations(
    val route: String, val icon: ImageVector? = null
) {
    object HomeScreen : Destinations(
        route = "home_screen", icon = Icons.Outlined.Home
    )

    object TrendingScreen : Destinations(
        route = "trending_screen/{page}", icon = Icons.AutoMirrored.Outlined.TrendingUp
    )

    object NotificationsScreen : Destinations(
        route = "notifications_screen", icon = Icons.Outlined.Notifications
    )

    object OwnProfile : Destinations(
        route = "own_profile_screen", icon = Icons.Outlined.AccountCircle
    )

    object Profile : Destinations(
        route = "profile_screen/{userid}", icon = Icons.Outlined.Favorite
    )

    object EditProfile : Destinations(
        route = "edit_profile_screen", icon = Icons.Outlined.Settings
    )

    object Preferences : Destinations(
        route = "preferences_screen", icon = Icons.Outlined.Settings
    )

    object MutedAccounts : Destinations(
        route = "muted_accounts_screen", icon = Icons.Outlined.Settings
    )

    object BlockedAccounts : Destinations(
        route = "blocked_accounts_screen", icon = Icons.Outlined.Settings
    )

    object LikedPosts : Destinations(
        route = "liked_posts_screen", icon = Icons.Outlined.Settings
    )

    object BookmarkedPosts : Destinations(
        route = "bookmarked_posts_screen", icon = Icons.Outlined.Settings
    )

    object FollowedHashtags : Destinations(
        route = "followed_hashtags_screen", icon = Icons.Outlined.Settings
    )

    object AboutInstance : Destinations(
        route = "about_instance_screen", icon = Icons.Outlined.Settings
    )

    object AboutPixelix : Destinations(
        route = "about_pixelix_screen", icon = Icons.Outlined.Settings
    )

    object NewPost : Destinations(
        route = "new_post_screen", icon = Icons.Outlined.Settings
    )

    object EditPost : Destinations(
        route = "edit_post_screen/{postId}", icon = Icons.Outlined.Settings
    )

    object Hashtag : Destinations(
        route = "hashtag_timeline_screen/{hashtag}", icon = Icons.Outlined.Settings
    )

    object SinglePost : Destinations(
        route = "single_post_screen/{postid}", icon = Icons.Outlined.Favorite
    )

    object Collection : Destinations(
        route = "collection_screen/{collectionid}", icon = Icons.Outlined.Favorite
    )

    object Followers : Destinations(
        route = "followers_screen/{page}/{userid}", icon = Icons.Outlined.Favorite
    )

    object Search : Destinations(
        route = "search_screen", icon = Icons.Outlined.Search
    )

    object Conversation : Destinations(
        route = "conversations", icon = Icons.Outlined.Home
    )

    object Chat : Destinations(
        route = "chat/{userid}", icon = Icons.Outlined.Home
    )
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController,
        startDestination = Destinations.HomeScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable(Destinations.HomeScreen.route) {
            HomeComposable(navController)
        }
        composable(Destinations.TrendingScreen.route) { navBackStackEntry ->
            val page = navBackStackEntry.arguments?.getString("page") ?: "posts"
            TrendingComposable(navController, page)
        }

        composable(Destinations.NotificationsScreen.route) {
            NotificationsComposable(navController)
        }

        composable(Destinations.Profile.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id ->
                OtherProfileComposable(navController, userId = id)
            }
        }

        composable(Destinations.Hashtag.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("hashtag")
            uId?.let { id ->
                HashtagTimelineComposable(navController, id)
            }
        }

        composable(Destinations.EditProfile.route) {
            EditProfileComposable(navController)
        }

        composable(Destinations.Preferences.route) {
            PreferencesComposable(navController)
        }

        composable(Destinations.NewPost.route) {
            NewPostComposable(navController)
        }

        composable(Destinations.EditPost.route) {navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getString("postId")
            postId?.let { id ->
                EditPostComposable(postId, navController)
            }
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

        composable(Destinations.AboutInstance.route) {
            AboutInstanceComposable(navController)
        }

        composable(Destinations.AboutPixelix.route) {
            AboutPixelixComposable(navController)
        }

        composable(Destinations.OwnProfile.route) {
            OwnProfileComposable(navController)
        }

        composable(Destinations.Followers.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            val page = navBackStackEntry.arguments?.getString("page")
            if (uId != null && page != null) {
                FollowersMainComposable(navController, accountId = uId, page = page)
            }
        }

        composable(Destinations.SinglePost.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("postid")
            uId?.let { id ->
                SinglePostComposable(navController, postId = id)
            }
        }

        composable(Destinations.Collection.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("collectionid")
            uId?.let { id ->
                CollectionComposable(navController, collectionId = id)
            }
        }

        composable(Destinations.Search.route) {
            SearchComposable(navController)
        }

        composable(Destinations.Conversation.route) {
            ConversationsComposable(navController = navController)
        }

        composable(Destinations.Chat.route) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("userid")
            uId?.let { id ->
                ChatComposable(navController = navController, accountId = id)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Destinations.HomeScreen,
        Destinations.Search,
        Destinations.TrendingScreen,
        Destinations.NotificationsScreen,
        Destinations.OwnProfile
    )

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(icon = {
                Icon(imageVector = screen.icon!!, contentDescription = "")
            }, selected = currentRoute == screen.route, onClick = {
                Navigate.navigateWithPopUp(screen.route, navController)
            })
        }
    }
}
