package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.InfinitePostsList

@Composable
fun HomeTimelineComposable(
    navController: NavController,
    viewModel: HomeTimelineViewModel = hiltViewModel()
) {
    InfinitePostsList(
        items = viewModel.homeTimelineState.homeTimeline,
        isLoading = viewModel.homeTimelineState.isLoading,
        isRefreshing = viewModel.homeTimelineState.refreshing,
        navController = navController,
        getItemsPaginated = {
            viewModel.getItemsPaginated()
        },
        onRefresh = {
            viewModel.refresh()
        }
    )
}