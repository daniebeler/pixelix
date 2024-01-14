package com.daniebeler.pixelix.ui.composables.trending.trending_accounts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.ui.composables.CustomAccount
import com.daniebeler.pixelix.ui.composables.CustomPullRefreshIndicator
import com.daniebeler.pixelix.ui.composables.ErrorComposable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TrendingAccountsComposable(
    navController: NavController, viewModel: TrendingAccountsViewModel = hiltViewModel()
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = viewModel.trendingAccountsState.isLoading,
            onRefresh = { viewModel.getTrendingAccountsState() })

    LazyColumn(modifier = Modifier.pullRefresh(pullRefreshState), content = {
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

    CustomPullRefreshIndicator(
        viewModel.trendingAccountsState.isLoading,
        pullRefreshState,
    )

    ErrorComposable(message = viewModel.trendingAccountsState.error)
}