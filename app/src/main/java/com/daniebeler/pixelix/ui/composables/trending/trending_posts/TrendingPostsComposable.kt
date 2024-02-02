package com.daniebeler.pixelix.ui.composables.trending.trending_posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.InfinitePostsGrid

@Composable
fun TrendingPostsComposable(
    range: String,
    navController: NavController,
    viewModel: TrendingPostsViewModel = hiltViewModel()
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
            emptyMessage = {
                Text(text = "no posts")
            },
            navController = navController
        ) {
            viewModel.getTrendingPosts(range, true)
        }
    }
}