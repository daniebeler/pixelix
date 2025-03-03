package com.daniebeler.pfpixelix.ui.composables.explore.trending.trending_posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.di.injectViewModel
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsGrid
import com.daniebeler.pfpixelix.ui.composables.InfinitePostsList
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.no_trending_posts

@Composable
fun TrendingPostsComposable(
    range: String,
    navController: NavController,
    viewModel: TrendingPostsViewModel = injectViewModel(key = "trending-posts") { trendingPostsViewModel }
) {

    DisposableEffect(range) {
        viewModel.getTrendingPosts(range)
        onDispose {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        InfinitePostsList(
            items = viewModel.trendingState.trendingPosts,
            isLoading = viewModel.trendingState.isLoading,
            isRefreshing = viewModel.trendingState.isRefreshing,
            error = viewModel.trendingState.error,
            endReached = false,
            view = ViewEnum.Grid,
            changeView = {  },
            isFirstItemLarge = true,
            itemGetsDeleted = {  },
            getItemsPaginated = {  },
            onRefresh = { viewModel.getTrendingPosts(range, true) },
            navController = navController,
            postGetsUpdated = {  },
            contentPaddingBottom = 10.dp
        )
    }
}