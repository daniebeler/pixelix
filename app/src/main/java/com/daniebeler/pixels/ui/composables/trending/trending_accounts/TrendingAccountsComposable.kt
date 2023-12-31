package com.daniebeler.pixels.ui.composables.trending.trending_accounts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable
import com.daniebeler.pixels.ui.composables.followers.CustomFollowerElement
import com.daniebeler.pixels.ui.composables.trending.trending_hashtags.CustomHashtag
import com.daniebeler.pixels.ui.composables.trending.trending_hashtags.TrendingHashtagsViewModel

@Composable
fun TrendingAccountsComposable(navController: NavController, viewModel: TrendingAccountsViewModel = hiltViewModel()) {
    LazyColumn(content = {
        items(viewModel.trendingAccountsState.trendingAccounts, key = {
            it.id
        }) {
            CustomFollowerElement(account = it, navController = navController)
        }
    })

    LoadingComposable(isLoading = viewModel.trendingAccountsState.isLoading)
    ErrorComposable(message = viewModel.trendingAccountsState.error)
}