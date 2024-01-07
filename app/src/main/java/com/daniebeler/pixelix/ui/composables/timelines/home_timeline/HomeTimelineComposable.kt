package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun HomeTimelineComposable(
    navController: NavController,
    viewModel: HomeTimelineViewModel = hiltViewModel()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.homeTimelineState.refreshing,
        onRefresh = { viewModel.refresh() }
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        items(viewModel.homeTimelineState.homeTimeline, key = {
            it.id
        }) { item ->
            PostComposable(post = item, navController)
        }

        if (viewModel.homeTimelineState.homeTimeline.isNotEmpty()) {
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
        viewModel.homeTimelineState.refreshing,
        pullRefreshState,
    )

    if (!viewModel.homeTimelineState.refreshing) {
        LoadingComposable(isLoading = viewModel.homeTimelineState.isLoading)
    }
    ErrorComposable(message = viewModel.homeTimelineState.error, pullRefreshState)

}