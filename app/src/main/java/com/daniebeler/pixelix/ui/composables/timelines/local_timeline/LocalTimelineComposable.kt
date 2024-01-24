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
        error = viewModel.localTimelineState.error,
        endReached = false,
        navController = navController,
        getItemsPaginated = {
            viewModel.getItemsPaginated()
        },
        onRefresh = {
            viewModel.refresh()
        },
        itemGetsDeleted = {postId ->  viewModel.postGetsDeleted(postId)}
    )
}