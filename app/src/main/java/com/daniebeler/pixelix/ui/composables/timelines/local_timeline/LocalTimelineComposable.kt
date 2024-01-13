package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfinitePostsList
import com.daniebeler.pixelix.ui.composables.LoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocalTimelineComposable(
    navController: NavController,
    viewModel: LocalTimelineViewModel = hiltViewModel()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.localTimelineState.refreshing,
        onRefresh = { viewModel.refresh() }
    )

    InfinitePostsList(
        items = viewModel.localTimelineState.localTimeline,
        isLoading = viewModel.localTimelineState.isLoading,
        isRefreshing = viewModel.localTimelineState.refreshing,
        navController = navController,
        getItemsPaginated = {
            viewModel.loadMorePosts(false)
        }
    )

    CustomPullRefreshIndicator(
        viewModel.localTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.localTimelineState.refreshing) {
        LoadingComposable(isLoading = viewModel.localTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.localTimelineState.error, pullRefreshState)
}