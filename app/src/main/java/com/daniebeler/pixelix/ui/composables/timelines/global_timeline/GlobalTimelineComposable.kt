package com.daniebeler.pixelix.ui.composables.timelines.global_timeline

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
fun GlobalTimelineComposable(
    navController: NavController,
    viewModel: GlobalTimelineViewModel = hiltViewModel()
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.globalTimelineState.refreshing,
        onRefresh = { viewModel.refresh() }
    )

    InfinitePostsList(
        items = viewModel.globalTimelineState.globalTimeline,
        isLoading = viewModel.globalTimelineState.isLoading,
        isRefreshing = viewModel.globalTimelineState.refreshing,
        navController = navController,
        getItemsPaginated = { viewModel.loadMorePosts(false) }
    )

    CustomPullRefreshIndicator(
        viewModel.globalTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.globalTimelineState.refreshing) {
        LoadingComposable(isLoading = viewModel.globalTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.globalTimelineState.error, pullRefreshState)
}