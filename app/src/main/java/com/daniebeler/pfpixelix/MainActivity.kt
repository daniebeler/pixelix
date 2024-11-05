package com.daniebeler.pfpixelix

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniebeler.pfpixelix.common.Destinations
import com.daniebeler.pfpixelix.di.HostSelectionInterceptorInterface
import com.daniebeler.pfpixelix.domain.model.LoginData
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.domain.usecase.GetCurrentLoginDataUseCase
import com.daniebeler.pfpixelix.domain.usecase.VerifyTokenUseCase
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
import com.daniebeler.pfpixelix.ui.composables.settings.icon_selection.IconSelectionComposable
import com.daniebeler.pfpixelix.ui.composables.settings.liked_posts.LikedPostsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.muted_accounts.MutedAccountsComposable
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.PreferencesComposable
import com.daniebeler.pfpixelix.ui.composables.single_post.SinglePostComposable
import com.daniebeler.pfpixelix.ui.composables.timelines.hashtag_timeline.HashtagTimelineComposable
import com.daniebeler.pfpixelix.ui.composables.trending.TrendingComposable
import com.daniebeler.pfpixelix.ui.theme.PixelixTheme
import com.daniebeler.pfpixelix.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var currentLoginDataUseCase: GetCurrentLoginDataUseCase

    @Inject
    lateinit var hostSelectionInterceptorInterface: HostSelectionInterceptorInterface

    @Inject
    lateinit var repository: CountryRepository

    @Inject
    lateinit var verifyTokenUseCase: VerifyTokenUseCase

    companion object {
        const val KEY_DESTINATION: String = "destination"
        const val KEY_DESTINATION_PARAM: String = "destination_parameter"

        enum class StartNavigation {
            Notifications, Profile, Post
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        runBlocking {
            val loginData: LoginData? = currentLoginDataUseCase()
            if (loginData == null || loginData.accessToken.isBlank() || loginData.baseUrl.isBlank()) {
                val oldBaseurl: String? = repository.getAuthV1Baseurl().firstOrNull()
                val oldAccessToken: String? = repository.getAuthV1Token().firstOrNull()
                if (oldBaseurl != null && oldAccessToken != null && oldBaseurl.isNotBlank() && oldAccessToken.isNotBlank()) {
                    repository.deleteAuthV1Data()
                    updateAuthToV2(this@MainActivity, oldBaseurl, oldAccessToken)
                } else {
                    gotoLoginActivity(this@MainActivity)
                }
            } else {
                if (loginData.accessToken.isNotEmpty()) {
                    hostSelectionInterceptorInterface.setToken(loginData.accessToken)
                }
                if (loginData.baseUrl.isNotEmpty()) {
                    hostSelectionInterceptorInterface.setHost(
                        loginData.baseUrl.replace(
                            "https://", ""
                        )
                    )
                }
            }
        }

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
                        val destination = intent.extras?.getString(KEY_DESTINATION) ?: ""
                        if (destination.isNotBlank()) {
                            // Delay the navigation action to ensure the graph is set
                            LaunchedEffect(Unit) {
                                when (destination) {
                                    StartNavigation.Notifications.toString() -> Navigate.navigate(
                                        "notifications_screen", navController
                                    )

                                    StartNavigation.Profile.toString() -> {
                                        val accountId: String = intent.extras?.getString(
                                            KEY_DESTINATION_PARAM
                                        ) ?: ""
                                        if (accountId.isNotBlank()) {
                                            Navigate.navigate(
                                                "profile_screen/$accountId", navController
                                            )
                                        }
                                    }

                                    StartNavigation.Post.toString() -> {
                                        val postId: String = intent.extras?.getString(
                                            KEY_DESTINATION_PARAM
                                        ) ?: ""
                                        if (postId.isNotBlank()) {
                                            Navigate.navigate(
                                                "single_post_screen/$postId", navController
                                            )

                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}

fun updateAuthToV2(context: Context, baseUrl: String, accessToken: String) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.putExtra("base_url", baseUrl)
    intent.putExtra("access_token", accessToken)
    context.startActivity(intent)
}

fun gotoLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
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
                OtherProfileComposable(navController, userId = id, byUsername = null)

            }
        }

        composable(Destinations.ProfileByUsername.route) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username")

            username?.let { username ->
                OtherProfileComposable(navController, userId = "", byUsername = username)
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

        composable(Destinations.IconSelection.route) {
            IconSelectionComposable(navController)
        }

        composable(Destinations.NewPost.route) {
            NewPostComposable(navController)
        }

        composable(Destinations.EditPost.route) { navBackStackEntry ->
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

        composable(
            "${Destinations.SinglePost.route}?refresh={refresh}",
            arguments = listOf(navArgument("refresh") {
                defaultValue = false
            })
        ) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("postid")
            val refresh = navBackStackEntry.arguments?.getBoolean("refresh")
            Log.d("refresh", refresh!!.toString())
            uId?.let { id ->
                SinglePostComposable(navController, postId = id, refresh)
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

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(icon = {
                if (currentRoute == screen.route) {
                    Icon(
                        imageVector = screen.activeIcon!!,
                        modifier = Modifier.size(32.dp),
                        contentDescription = ""
                    )
                } else {
                    Icon(
                        imageVector = screen.icon!!,
                        modifier = Modifier.size(32.dp),
                        contentDescription = ""
                    )
                }
            },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, indicatorColor = Color.Transparent
                ),
                onClick = {
                    Navigate.navigateWithPopUp(screen.route, navController)
                })
        }
    }
}
