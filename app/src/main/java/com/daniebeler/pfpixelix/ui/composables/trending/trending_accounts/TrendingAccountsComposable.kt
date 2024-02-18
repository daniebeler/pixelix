package com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.ui.composables.custom_account.CustomAccount
import com.daniebeler.pfpixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenEmptyStateComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenErrorComposable
import com.daniebeler.pfpixelix.ui.composables.states.FullscreenLoadingComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingAccountsComposable(
    navController: NavController, viewModel: TrendingAccountsViewModel = hiltViewModel()
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.trendingAccountsState.isRefreshing,
            onRefresh = { viewModel.getTrendingAccountsState(true) })

    LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState).fillMaxSize(), content = {
        items(viewModel.trendingAccountsState.trendingAccounts, key = {
            it.id
        }) {
            CustomAccount(
                account = it,
                relationship = viewModel.accountRelationShipsState.accountRelationships.find { relationship ->
                    relationship.id == it.id
                },
                navController = navController
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
            FullscreenEmptyStateComposable()
        }
    }

    CustomPullRefreshIndicator(
        viewModel.trendingAccountsState.isRefreshing,
        pullRefreshState,
    )
}