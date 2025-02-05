package com.daniebeler.pfpixelix.ui.composables.explore.trending.trending_hashtags

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import com.daniebeler.pfpixelix.di.injectViewModel
import androidx.navigation.NavController
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrendingHashtagsComposable(
    navController: NavController,
    viewModel: TrendingHashtagsViewModel = injectViewModel(key = "trending-hashtags-key") { trendingHashtagsViewModel }
) {
    PullToRefreshBox(
        isRefreshing = viewModel.trendingHashtagsState.isRefreshing,
        onRefresh = { viewModel.getTrendingHashtags(true) },
    ) {
    LazyColumn(modifier = Modifier
        .fillMaxSize(), content = {
        items(viewModel.trendingHashtagsState.trendingHashtags, key = {
            it.hashtag ?: ""
        }) {
            it.name = it.hashtag ?: ""
            TrendingHashtagElement(hashtag = it, navController = navController)
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
            FullscreenEmptyStateComposable(EmptyState(heading = stringResource(Res.string.no_trending_hashtags)))
        }
    }
}}