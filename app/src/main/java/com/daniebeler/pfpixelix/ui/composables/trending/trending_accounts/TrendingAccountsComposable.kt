package com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.states.EmptyState
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingAccountsComposable(
    navController: NavController,
    viewModel: TrendingAccountsViewModel = hiltViewModel(key = "trending-accounts-key")
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.trendingAccountsState.isRefreshing,
            onRefresh = { viewModel.getTrendingAccountsState(true) })

    LazyColumn(modifier = Modifier
        .pullRefresh(pullRefreshState)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        content = {
            items(viewModel.trendingAccountsState.trendingAccounts, key = {
                it.id
            }) {
                TrendingAccountElement(
                    account = it, navController = navController
                )
            }
        })

    if (viewModel.trendingAccountsState.trendingAccounts.isEmpty()) {
        if (viewModel.trendingAccountsState.isLoading && !viewModel.trendingAccountsState.isRefreshing) {
            FullscreenLoadingComposable()
        }

        if (viewModel.trendingAccountsState.error.isNotEmpty()) {
            FullscreenErrorComposable(message = viewModel.trendingAccountsState.error)
        }

        if (!viewModel.trendingAccountsState.isLoading && viewModel.trendingAccountsState.error.isEmpty()) {
            FullscreenEmptyStateComposable(EmptyState(heading = stringResource(R.string.no_trending_profiles)))
        }
    }

    CustomPullRefreshIndicator(
        viewModel.trendingAccountsState.isRefreshing,
        pullRefreshState,
    )
}