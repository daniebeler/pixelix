package com.daniebeler.pixels.ui.composables.trending.trending_accounts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixels.ui.composables.CustomAccount
import com.daniebeler.pixels.ui.composables.ErrorComposable
import com.daniebeler.pixels.ui.composables.LoadingComposable

@Composable
fun TrendingAccountsComposable(
    navController: NavController,
    viewModel: TrendingAccountsViewModel = hiltViewModel()
) {
    LazyColumn(content = {
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

    LoadingComposable(isLoading = viewModel.trendingAccountsState.isLoading)
    ErrorComposable(message = viewModel.trendingAccountsState.error)
}