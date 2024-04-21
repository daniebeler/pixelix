package com.daniebeler.pfpixelix.ui.composables.followers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.EndOfListComposable
import com.daniebeler.pfpixelix.ui.composables.states.ErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.LoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersComposable(
    navController: NavController,
    viewModel: FollowersViewModel = hiltViewModel(key = "followers-viewmodel-key")
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.followersState.isRefreshing,
            onRefresh = { viewModel.getFollowersFirstLoad(true) })

    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState, content = {
        items(viewModel.followersState.followers, key = {
            it.id
        }) {
            FollowerElementComposable(account = it, navController)
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

    if (!viewModel.followersState.isLoading && viewModel.followersState.error.isEmpty() && viewModel.followersState.followers.isEmpty()) {
        FullscreenEmptyStateComposable(
            emptyState = EmptyState(
                icon = Icons.Outlined.Groups,
                heading = stringResource(R.string.empty),
                message = stringResource(R.string.nobody_follows_you_yet)
            )
        )
    }

    InfiniteListHandler(lazyListState = lazyListState) {
        //viewModel.getFollowersPaginated()
    }

    CustomPullRefreshIndicator(
        viewModel.followersState.isRefreshing, pullRefreshState
    )

    LoadingComposable(isLoading = viewModel.followersState.isLoading && viewModel.followersState.followers.isEmpty())
    ErrorComposable(message = viewModel.followersState.error)
}