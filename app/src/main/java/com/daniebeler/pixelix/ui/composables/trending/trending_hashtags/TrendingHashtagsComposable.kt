package com.daniebeler.pixelix.ui.composables.trending.trending_hashtags

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomHashtag
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingHashtagsComposable(
    navController: NavController, viewModel: TrendingHashtagsViewModel = hiltViewModel()
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.trendingHashtagsState.isRefreshing,
            onRefresh = { viewModel.getTrendingHashtags(true) })

    LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState), content = {
        items(viewModel.trendingHashtagsState.trendingHashtags, key = {
            it.hashtag ?: ""
        }) {
            it.name = it.hashtag ?: ""
            CustomHashtag(hashtag = it, navController = navController)
        }
    })

    if (viewModel.trendingHashtagsState.trendingHashtags.isEmpty()) {
        if (viewModel.trendingHashtagsState.isLoading && !viewModel.trendingHashtagsState.isRefreshing) {
            FullscreenLoadingComposable()
        }

        if (viewModel.trendingHashtagsState.error.isNotEmpty()) {
            FullscreenErrorComposable(message = viewModel.trendingHashtagsState.error)
        }

        if (!viewModel.trendingHashtagsState.isLoading && viewModel.trendingHashtagsState.error.isEmpty()) {
            FullscreenEmptyStateComposable()
        }
    }

    CustomPullRefreshIndicator(
        viewModel.trendingHashtagsState.isRefreshing,
        pullRefreshState,
    )
}