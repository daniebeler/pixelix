package com.daniebeler.pixels.ui.composables.trending.trending_hashtags

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.CustomHashtag
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable

@Composable
fun TrendingHashtagsComposable(
    navController: NavController,
    viewModel: TrendingHashtagsViewModel = hiltViewModel()
) {

    LazyColumn(content = {
        items(viewModel.trendingHashtagsState.trendingHashtags, key = {
            it.name
        }) {
            CustomHashtag(hashtag = it, navController = navController)
        }
    })

    LoadingComposable(isLoading = viewModel.trendingHashtagsState.isLoading)
    ErrorComposable(message = viewModel.trendingHashtagsState.error)
}