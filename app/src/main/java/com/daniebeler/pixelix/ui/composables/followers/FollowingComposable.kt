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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomAccount
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.EndOfListComposable
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.LoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowingComposable(
    navController: NavController,
    viewModel: FollowersViewModel = hiltViewModel()
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.followingState.isRefreshing,
        onRefresh = { viewModel.getFollowingFirstLoad(true) }
    )

    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState, content = {
        items(viewModel.followingState.following, key = {
            it.id
        }) {
            CustomAccount(account = it, null, navController)
        }

        if (viewModel.followingState.following.isNotEmpty() && viewModel.followingState.isLoading && !viewModel.followingState.isRefreshing) {
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

        if (viewModel.followingState.endReached && viewModel.followingState.following.size > 10) {
            item {
                EndOfListComposable()
            }
        }
    })

    InfiniteListHandler(lazyListState = lazyListState) {
        viewModel.getFollowingPaginated()
    }

    CustomPullRefreshIndicator(
        viewModel.followingState.isRefreshing,
        pullRefreshState
    )

    LoadingComposable(isLoading = viewModel.followingState.isLoading && viewModel.followingState.following.isEmpty())
    ErrorComposable(message = viewModel.followingState.error)
}