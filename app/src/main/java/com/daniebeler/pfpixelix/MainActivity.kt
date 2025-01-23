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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
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
import com.daniebeler.pfpixelix.ui.composables.mention.MentionComposable
import com.daniebeler.pfpixelix.ui.composables.newpost.NewPostComposable
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsComposable
import com.daniebeler.pfpixelix.ui.composables.profile.other_profile.OtherProfileComposable
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.AccountSwitchBottomSheet
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
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        var avatar = ""
        runBlocking {
            val loginData: LoginData? = currentLoginDataUseCase()
            if (loginData == null || loginData.accessToken.isBlank() || loginData.baseUrl.isBlank()) {
                val oldBaseurl: String? = repository.getAuthV1Baseurl().firstOrNull()
                val oldAccessToken: String? = repository.getAuthV1Token().firstOrNull()
                if (oldBaseurl != null && oldAccessToken != null && oldBaseurl.isNotBlank() && oldAccessToken.isNotBlank()) {
                    repository.deleteAuthV1Data()
                    updateAuthToV2(this@MainActivity, oldBaseurl, oldAccessToken)
                } else {
                    gotoLoginActivity(this@MainActivity, false)
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
                avatar = loginData.avatar
            }
        }

        setContent {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var showAccountSwitchBottomSheet by remember { mutableStateOf(false) }

            PixelixTheme {
                val navController: NavHostController = rememberNavController()

                Scaffold(bottomBar = {
                    BottomBar(navController = navController,
                        avatar = avatar,
                        openAccountSwitchBottomSheet = { showAccountSwitchBottomSheet = true })
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
                if (showAccountSwitchBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showAccountSwitchBottomSheet = false
                        }, sheetState = sheetState
                    ) {
                        AccountSwitchBottomSheet(closeBottomSheet = {
                            showAccountSwitchBottomSheet = false
                        }, null)
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

fun gotoLoginActivity(context: Context, isAbleToGotBack: Boolean) {
    val intent = if (isAbleToGotBack) {
        Intent(context, LoginActivity::class.java)
    } else {
        Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
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

            username?.let {
                OtherProfileComposable(navController, userId = "", byUsername = it)
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
            "${Destinations.SinglePost.route}?refresh={refresh}&openReplies={openReplies}",
            arguments = listOf(navArgument("refresh") {
                defaultValue = false
            }, navArgument("openReplies") {
                defaultValue = false
            })
        ) { navBackStackEntry ->
            val uId = navBackStackEntry.arguments?.getString("postid")
            val refresh = navBackStackEntry.arguments?.getBoolean("refresh")
            val openReplies = navBackStackEntry.arguments?.getBoolean("openReplies")
            Log.d("refresh", refresh!!.toString())
            Log.d("openReplies", openReplies!!.toString())
            uId?.let { id ->
                SinglePostComposable(navController, postId = id, refresh, openReplies)
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

        composable(Destinations.Mention.route) { navBackStackEntry ->
            val mentionId = navBackStackEntry.arguments?.getString("mentionid")
            mentionId?.let { id ->
                MentionComposable(navController = navController, mentionId = id)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomBar(
    navController: NavHostController, avatar: String, openAccountSwitchBottomSheet: () -> Unit
) {
    val screens = listOf(
        Destinations.HomeScreen,
        Destinations.Search,
        Destinations.TrendingScreen,
        Destinations.NotificationsScreen,
        Destinations.OwnProfile
    )

    NavigationBar(Modifier.height(90.dp)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            val interactionSource = remember { MutableInteractionSource() }
            val coroutineScope = rememberCoroutineScope()
            var isLongPress by remember { mutableStateOf(false) }

            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> {
                            isLongPress = false // Reset flag before starting detection
                            coroutineScope.launch {
                                delay(500L) // Long-press threshold
                                if (screen.route == Destinations.OwnProfile.route) {
                                    openAccountSwitchBottomSheet()
                                }
                                isLongPress = true
                            }
                        }

                        is PressInteraction.Release, is PressInteraction.Cancel -> {
                            coroutineScope.coroutineContext.cancelChildren()
                        }
                    }
                }
            }
            NavigationBarItem(icon = {
                if (screen.route == Destinations.OwnProfile.route && avatar.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .clip(CircleShape)
                        )
                        Icon(Icons.Outlined.UnfoldMore, contentDescription = "long press to switch account")
                    }
                } else if (currentRoute == screen.route) {
                    Icon(
                        imageVector = screen.activeIcon!!,
                        modifier = Modifier.size(30.dp),
                        contentDescription = stringResource(screen.label)
                    )
                } else {
                    Icon(
                        imageVector = screen.icon!!,
                        modifier = Modifier.size(30.dp),
                        contentDescription = stringResource(screen.label)
                    )
                }
            },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.inverseSurface,
                    indicatorColor = Color.Transparent
                ),
                interactionSource = interactionSource,
                onClick = {
                    if (!isLongPress) {
                        Navigate.navigateWithPopUp(screen.route, navController)
                    }
                })
        }
    }
}
