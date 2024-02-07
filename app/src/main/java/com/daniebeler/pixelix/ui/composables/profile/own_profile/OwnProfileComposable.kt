package com.daniebeler.pixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pixelix.ui.composables.profile.AccountState
import com.daniebeler.pixelix.ui.composables.profile.PostsState
import com.daniebeler.pixelix.ui.composables.profile.ProfileTopSection
import com.daniebeler.pixelix.utils.Navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnProfileComposable(
    navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(text = viewModel.accountState.account?.username ?: "")
        }, actions = {
            IconButton(onClick = {
                //Navigate().navigate("settings_screen", navController)
                showBottomSheet = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings, contentDescription = ""
                )
            }
        })

    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            Navigate().navigate("new_post_screen", navController)
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            CustomProfilePage(accountState = viewModel.accountState,
                postsState = viewModel.postsState,
                navController = navController,
                refresh = {
                    viewModel.loadData()
                },
                getPostsPaginated = {
                    viewModel.getPostsPaginated()
                },
                otherAccountTopSectionAdditions = {})
        }


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState
            ) {
                ModalBottomSheetContent(navController = navController, closeBottomSheet = {
                    showBottomSheet = false
                })
            }
        }
    }
}

@Composable
fun CustomProfilePage(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit,
    otherAccountTopSectionAdditions: @Composable () -> Unit
) {
    Box {
        InfinitePostsGrid(items = postsState.posts,
            isLoading = postsState.isLoading,
            isRefreshing = accountState.isLoading && accountState.account != null,
            error = postsState.error,
            emptyMessage = {
                Text(text = stringResource(R.string.no_posts_yet))
            },
            endReached = postsState.endReached,
            navController = navController,
            getItemsPaginated = { getPostsPaginated() },
            before = {
                Column {
                    ProfileTopSection(
                        account = accountState.account, navController
                    )

                    otherAccountTopSectionAdditions()
                }

            }) { refresh() }
    }
}

@Composable
private fun ModalBottomSheetContent(navController: NavController, closeBottomSheet: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {

        CustomSettingsElement(icon = Icons.Outlined.Settings,
            text = stringResource(R.string.settings),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("preferences_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.liked_posts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("liked_posts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Bookmarks,
            text = stringResource(R.string.bookmarked_posts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("bookmarked_posts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Tag,
            text = stringResource(R.string.followed_hashtags),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("followed_hashtags_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.DoNotDisturbOn,
            text = stringResource(R.string.muted_accounts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("muted_accounts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Block,
            text = stringResource(R.string.blocked_accounts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("blocked_accounts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Dns,
            text = stringResource(R.string.about_this_instance),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("about_instance_screen", navController)
            })

    }
}

@Composable
fun CustomSettingsElement(
    icon: ImageVector, text: String, smallText: String = "", onClick: () -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp)
            }
        }
    }
}