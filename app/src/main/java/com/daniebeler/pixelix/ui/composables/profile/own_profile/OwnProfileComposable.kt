package com.daniebeler.pixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0), title = {
            Text(text = viewModel.accountState.account?.username ?: "")
        }, actions = {
            IconButton(onClick = {
                Navigate().navigate("settings_screen", navController)
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