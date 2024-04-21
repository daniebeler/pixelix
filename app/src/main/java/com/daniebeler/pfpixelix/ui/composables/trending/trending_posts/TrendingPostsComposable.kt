package com.daniebeler.pfpixelix.ui.composables.trending.trending_posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState

@Composable
fun TrendingPostsComposable(
    range: String,
    navController: NavController,
    viewModel: TrendingPostsViewModel = hiltViewModel(key = "trending-posts")
) {

    DisposableEffect(range) {
        viewModel.getTrendingPosts(range)
        onDispose {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        InfinitePostsGrid(
            items = viewModel.trendingState.trendingPosts,
            isLoading = viewModel.trendingState.isLoading,
            isRefreshing = viewModel.trendingState.isRefreshing,
            endReached = true,
            error = viewModel.trendingState.error,
            emptyMessage = EmptyState(heading = stringResource(R.string.no_trending_posts)),
            navController = navController
        ) {
            viewModel.getTrendingPosts(range, true)
        }
    }
}