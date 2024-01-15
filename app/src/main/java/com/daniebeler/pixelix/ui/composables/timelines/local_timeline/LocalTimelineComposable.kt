package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.InfinitePostsList

@Composable
fun LocalTimelineComposable(
    navController: NavController,
    viewModel: LocalTimelineViewModel = hiltViewModel()
) {
    InfinitePostsList(
        items = viewModel.localTimelineState.localTimeline,
        isLoading = viewModel.localTimelineState.isLoading,
        isRefreshing = viewModel.localTimelineState.refreshing,
        navController = navController,
        getItemsPaginated = {
            viewModel.loadMorePosts(false)
        },
        onRefresh = {
            viewModel.refresh()
        }
    )
}