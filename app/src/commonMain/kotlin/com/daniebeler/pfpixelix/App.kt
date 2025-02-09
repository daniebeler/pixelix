package com.daniebeler.pfpixelix

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import com.daniebeler.pfpixelix.common.Destinations
import com.daniebeler.pfpixelix.di.AppComponent
import com.daniebeler.pfpixelix.di.LocalAppComponent
import com.daniebeler.pfpixelix.ui.composables.HomeComposable
import com.daniebeler.pfpixelix.ui.composables.ReverseModalNavigationDrawer
import com.daniebeler.pfpixelix.ui.composables.collection.CollectionComposable
import com.daniebeler.pfpixelix.ui.composables.direct_messages.chat.ChatComposable
import com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations.ConversationsComposable
import com.daniebeler.pfpixelix.ui.composables.edit_post.EditPostComposable
import com.daniebeler.pfpixelix.ui.composables.edit_profile.EditProfileComposable
import com.daniebeler.pfpixelix.ui.composables.explore.ExploreComposable
import com.daniebeler.pfpixelix.ui.composables.followers.FollowersMainComposable
import com.daniebeler.pfpixelix.ui.composables.mention.MentionComposable
import com.daniebeler.pfpixelix.ui.composables.newpost.NewPostComposable
import com.daniebeler.pfpixelix.ui.composables.notifications.NotificationsComposable
import com.daniebeler.pfpixelix.ui.composables.profile.other_profile.OtherProfileComposable
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.AccountSwitchBottomSheet
import com.daniebeler.pfpixelix.ui.composables.profile.own_profile.OwnProfileComposable
import com.daniebeler.pfpixelix.ui.composables.session.LoginComposable
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
import com.daniebeler.pfpixelix.ui.theme.PixelixTheme
import com.daniebeler.pfpixelix.utils.KmpUri
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.end
import com.daniebeler.pfpixelix.utils.toKmpUri
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.default_avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    appComponent: AppComponent,
    exitApp: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    DisposableEffect(uriHandler) {
        val systemUrlHandler = appComponent.systemUrlHandler
        systemUrlHandler.uriHandler = uriHandler
        onDispose {
            systemUrlHandler.uriHandler = null
        }
    }
    CompositionLocalProvider(
        LocalAppComponent provides appComponent
    ) {
        PixelixTheme {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var showAccountSwitchBottomSheet by remember { mutableStateOf(false) }

            var activeUser by remember { mutableStateOf<String?>("unknown") }
            LaunchedEffect(Unit) {
                val authService = appComponent.authService
                authService.openSessionIfExist()
                authService.activeUser.collect {
                    activeUser = it
                }
            }
            if (activeUser == "unknown") return@PixelixTheme

            ReverseModalNavigationDrawer(
                gesturesEnabled = drawerState.isOpen,
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerState = drawerState,
                        drawerShape = shapes.extraLarge.end(0.dp),
                    ) {
                        PreferencesComposable(navController, drawerState, {
                            scope.launch {
                                drawerState.close()
                            }
                        })
                    }
                }) {

                Scaffold(
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            openAccountSwitchBottomSheet = {
                                showAccountSwitchBottomSheet = true
                            },
                        )
                    },
                    content = { paddingValues ->
                        NavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = Destinations.FirstLogin.route,
                            builder = {
                                navigationGraph(
                                    navController,
                                    { scope.launch { drawerState.open() } },
                                    exitApp
                                )
                            }
                        )
                        LaunchedEffect(activeUser) {
                            val rootScreen = if (activeUser == null) {
                                Destinations.FirstLogin.route
                            } else {
                                Destinations.HomeScreen.route
                            }
                            navController.navigate(rootScreen) {
                                val root = navController.currentBackStack.value
                                    .firstOrNull { it.destination.route != null }
                                    ?.destination?.route
                                if (root != null) {
                                    popUpTo(root) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
            if (showAccountSwitchBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showAccountSwitchBottomSheet = false
                    }, sheetState = sheetState
                ) {
                    AccountSwitchBottomSheet(
                        navController = navController,
                        closeBottomSheet = { showAccountSwitchBottomSheet = false },
                        null
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.navigationGraph(
    navController: NavHostController,
    openPreferencesDrawer: () -> Unit,
    exitApp: () -> Unit
) {
    dialog(
        route = Destinations.FirstLogin.route,
    ) {
        Dialog(
            onDismissRequest = exitApp,
            properties = DialogProperties(
                dismissOnClickOutside = false
            )
        ) {
            SetUpEdgeToEdgeDialog()
            LoginComposable()
        }
    }
    dialog(
        route = Destinations.NewLogin.route,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        SetUpEdgeToEdgeDialog()
        LoginComposable()
    }

    composable(Destinations.HomeScreen.route) {
        HomeComposable(navController, openPreferencesDrawer)
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

    composable(Destinations.IconSelection.route) {
        IconSelectionComposable(navController)
    }

    composable("${Destinations.NewPost.route}?uris={uris}") { navBackStackEntry ->
        val urisJson = navBackStackEntry.arguments?.getString("uris")
        val imageUris: List<KmpUri>? = urisJson?.let { json ->
            Json.decodeFromString<List<String>>(json).map { it.toKmpUri() }
        }
        NewPostComposable(navController, imageUris)
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
        OwnProfileComposable(navController, openPreferencesDrawer)
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
        val refresh = navBackStackEntry.arguments?.getBoolean("refresh")!!
        val openReplies = navBackStackEntry.arguments?.getBoolean("openReplies")!!
        Logger.d("refresh") { refresh.toString() }
        Logger.d("openReplies") { openReplies.toString() }
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
        ExploreComposable(navController)
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

@Composable
private fun BottomBar(
    navController: NavHostController,
    openAccountSwitchBottomSheet: () -> Unit
) {
    val screens = listOf(
        Destinations.HomeScreen,
        Destinations.Search,
        Destinations.NewPost,
        Destinations.NotificationsScreen,
        Destinations.OwnProfile
    )
    val systemNavigationBarHeight =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    NavigationBar(
        modifier = Modifier.height(60.dp + systemNavigationBarHeight)
    ) {
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
                var avatar by mutableStateOf<String?>(null)
                val appComponent = LocalAppComponent.current
                LaunchedEffect(Unit) {
                    val authService = appComponent.authService
                    authService.activeUser
                        .map { authService.getCurrentSession() }
                        .collect { avatar = it?.avatar }
                }


                if (screen.route == Destinations.OwnProfile.route && avatar != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = avatar,
                            error = painterResource(Res.drawable.default_avatar),
                            contentDescription = "",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .clip(CircleShape)
                        )
                        Icon(
                            Icons.Outlined.UnfoldMore,
                            contentDescription = "long press to switch account"
                        )
                    }
                } else if (currentRoute?.startsWith(screen.route) == true) {
                    Icon(
                        imageVector = vectorResource(screen.activeIcon),
                        modifier = Modifier.size(30.dp),
                        contentDescription = stringResource(screen.label)
                    )
                } else {
                    Icon(
                        imageVector = vectorResource(screen.icon),
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

//https://partnerissuetracker.corp.google.com/issues/246909281
@Composable
expect fun SetUpEdgeToEdgeDialog()
