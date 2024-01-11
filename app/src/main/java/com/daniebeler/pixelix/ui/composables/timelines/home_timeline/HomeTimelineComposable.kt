package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.InfiniteListHandler
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.post.PostComposable

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

    val lazyListState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.pullRefresh(pullRefreshState),
        state = lazyListState
    ) {
        items(viewModel.homeTimelineState.homeTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }

        if (viewModel.homeTimelineState.homeTimeline.isNotEmpty() && viewModel.homeTimelineState.isLoading && !viewModel.homeTimelineState.refreshing) {
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
    }

    InfiniteListHandler(lazyListState = lazyListState) {
        viewModel.getItemsPaginated()
    }

    CustomPullRefreshIndicator(
        viewModel.homeTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.homeTimelineState.refreshing && viewModel.homeTimelineState.homeTimeline.isEmpty()) {
        LoadingComposable(isLoading = viewModel.homeTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.homeTimelineState.error, pullRefreshState)

}