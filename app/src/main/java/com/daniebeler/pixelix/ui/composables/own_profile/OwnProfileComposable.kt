package com.daniebeler.pixelix.ui.composables.own_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPost
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteGridHandler
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.profile.AccountState
import com.daniebeler.pixelix.ui.composables.profile.PostsState
import com.daniebeler.pixelix.ui.composables.profile.ProfileTopSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnProfileComposable(
    navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()
) {


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = viewModel.accountState.account?.username ?: "")
        }, actions = {
            IconButton(onClick = {
                navController.navigate("settings_screen") {
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Settings, contentDescription = ""
                )
            }
        })

    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate("new_post_screen") {
                launchSingleTop = true
                restoreState = true
            }
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

            CustomProfilePage(
                accountState = viewModel.accountState,
                postsState = viewModel.postsState,
                navController = navController,
                refresh = {
                    viewModel.loadData()
                },
                getPostsPaginated = {
                    viewModel.getPostsPaginated()
                }
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomProfilePage(
    accountState: AccountState,
    postsState: PostsState,
    navController: NavController,
    refresh: () -> Unit,
    getPostsPaginated: () -> Unit
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = accountState.isLoading || postsState.isLoading,
            onRefresh = { refresh() })

    Column {
        if (accountState.account != null) {

            InfinitePostsGrid(
                items = postsState.posts,
                isLoading = postsState.isLoading,
                isRefreshing = false,
                navController = navController,
                getItemsPaginated = { getPostsPaginated() },
                before = {
                    if (accountState.account != null) {
                        ProfileTopSection(
                            account = accountState.account!!, navController
                        )
                    }
                })

            if (postsState.posts.isEmpty() && !postsState.isLoading && postsState.error.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp, 20.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_state_no_posts),
                        contentDescription = null,
                        Modifier.fillMaxWidth()
                    )
                }
            }
        }

        //LoadingComposable(isLoading = viewModel.accountState.isLoading)
        ErrorComposable(message = accountState.error, pullRefreshState)
    }

    CustomPullRefreshIndicator(
        accountState.isLoading || postsState.isLoading,
        pullRefreshState,
    )
}