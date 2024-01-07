package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable
import com.daniebeler.pixelix.ui.composables.LoadingComposable
import com.daniebeler.pixelix.ui.composables.post.PostComposable

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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        items(viewModel.localTimelineState.localTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }

        if (viewModel.localTimelineState.localTimeline.isNotEmpty()) {
            item {
                Button(onClick = {
                    viewModel.loadMorePosts(false)
                }) {
                    Text(text = stringResource(R.string.load_more))
                }
            }
        }
    }
    CustomPullRefreshIndicator(
        viewModel.localTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.localTimelineState.refreshing) {
        LoadingComposable(isLoading = viewModel.localTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.localTimelineState.error, pullRefreshState)
}