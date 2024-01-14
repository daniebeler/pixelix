package com.daniebeler.pixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable
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
    Scaffold(topBar = {
        TopAppBar(title = {
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
                error = postsState.error,
                emptyMessage = {
                    Text(text = "no posts")
                },
                endReached = postsState.endReached,
                navController = navController,
                getItemsPaginated = { getPostsPaginated() },
                before = {
                    ProfileTopSection(
                        account = accountState.account, navController
                    )
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