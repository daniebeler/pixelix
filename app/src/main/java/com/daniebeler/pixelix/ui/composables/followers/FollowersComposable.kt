package com.daniebeler.pixelix.ui.composables.followers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomAccount
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.states.LoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersComposable(
    navController: NavController,
    viewModel: FollowersViewModel = hiltViewModel()
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.followersState.isRefreshing,
        onRefresh = { viewModel.getFollowersFirstLoad(true) }
    )

    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState,
        content = {
            items(viewModel.followersState.followers, key = {
                it.id
            }) {
                CustomAccount(account = it, null, navController)
            }

            if (viewModel.followersState.followers.isNotEmpty() && viewModel.followersState.isLoading && !viewModel.followersState.isRefreshing) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }

            if (viewModel.followersState.endReached && viewModel.followersState.followers.size > 10) {
                item {
                    EndOfListComposable()
                }
            }
        })

    InfiniteListHandler(lazyListState = lazyListState) {
        viewModel.getFollowersPaginated()
    }

    CustomPullRefreshIndicator(
        viewModel.followersState.isRefreshing,
        pullRefreshState
    )

    LoadingComposable(isLoading = viewModel.followersState.isLoading && viewModel.followersState.followers.isEmpty())
    ErrorComposable(message = viewModel.followersState.error)
}