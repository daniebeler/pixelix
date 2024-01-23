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
import com.daniebeler.pixelix.ui.composables.ErrorComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingHashtagsComposable(
    navController: NavController, viewModel: TrendingHashtagsViewModel = hiltViewModel()
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.trendingHashtagsState.isLoading,
            onRefresh = { viewModel.getTrendingHashtags() })

    LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState), content = {
        items(viewModel.trendingHashtagsState.trendingHashtags, key = {
            it.hashtag ?: ""
        }) {
            it.name = it.hashtag ?: ""
            CustomHashtag(hashtag = it, navController = navController)
        }
    })

    CustomPullRefreshIndicator(
        viewModel.trendingHashtagsState.isLoading,
        pullRefreshState,
    )

    ErrorComposable(message = viewModel.trendingHashtagsState.error)
}