package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

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
fun HomeTimelineComposable(
    navController: NavController,
    viewModel: HomeTimelineViewModel = hiltViewModel()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.homeTimelineState.refreshing,
        onRefresh = { viewModel.refresh() }
    )

    InfinitePostsList(
        items = viewModel.homeTimelineState.homeTimeline,
        isLoading = viewModel.homeTimelineState.isLoading,
        isRefreshing = viewModel.homeTimelineState.refreshing,
        navController = navController,
        getItemsPaginated = {
            viewModel.getItemsPaginated()
        }
    )

    CustomPullRefreshIndicator(
        viewModel.homeTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.homeTimelineState.refreshing && viewModel.homeTimelineState.homeTimeline.isEmpty()) {
        LoadingComposable(isLoading = viewModel.homeTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.homeTimelineState.error, pullRefreshState)

}