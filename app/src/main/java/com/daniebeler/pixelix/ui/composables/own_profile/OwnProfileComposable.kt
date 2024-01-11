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
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPost
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteGridHandler
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.profile.ProfileTopSection

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OwnProfileComposable(
    navController: NavController, viewModel: OwnProfileViewModel = hiltViewModel()
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.accountState.isLoading || viewModel.postsState.isLoading,
            onRefresh = { viewModel.loadData() })

    val lazyGridState = rememberLazyGridState()

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
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                if (viewModel.accountState.account != null) {
                    LazyVerticalGrid(columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        state = lazyGridState,
                        modifier = Modifier
                            .padding(paddingValues)
                            .pullRefresh(pullRefreshState),
                        content = {
                            if (viewModel.accountState.account != null) {
                                item(span = { GridItemSpan(3) }) {
                                    ProfileTopSection(
                                        account = viewModel.accountState.account!!, navController
                                    )
                                }
                            }
                            if (viewModel.postsState.posts.isNotEmpty()) {
                                items(viewModel.postsState.posts, key = {
                                    it.id
                                }) { photo ->
                                    CustomPost(post = photo, navController = navController)
                                }
                            }/*item {
                                Button(onClick = {
                                    //viewModel.loadMorePosts()
                                }) {
                                    Text(text = stringResource(R.string.load_more))
                                }
                            }*/

                            if (viewModel.postsState.posts.isNotEmpty() && viewModel.postsState.isLoading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .wrapContentSize(Alignment.Center),
                                        color = MaterialTheme.colorScheme.secondary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    )
                                }
                            }

                            if (viewModel.postsState.endReached && viewModel.postsState.posts.size > 10) {
                                item {
                                    EndOfListComposable()
                                }
                            }
                        })

                    InfiniteGridHandler(lazyGridState = lazyGridState, buffer = 6) {
                        viewModel.getPostsPaginated()
                    }

                    if (viewModel.postsState.posts.isEmpty() && !viewModel.postsState.isLoading && viewModel.postsState.error.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(36.dp, 20.dp)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                                .pullRefresh(pullRefreshState)
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
                ErrorComposable(message = viewModel.accountState.error, pullRefreshState)
            }
            CustomPullRefreshIndicator(
                viewModel.accountState.isLoading || viewModel.postsState.isLoading,
                pullRefreshState,
            )
        }
    }
}