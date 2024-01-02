package com.daniebeler.pixelix.ui.composables.trending.trending_accounts

import com.daniebeler.pixelix.domain.model.Account

data class TrendingAccountsState(
    val isLoading: Boolean = false,
    val trendingAccounts: List<Account> = emptyList(),
    val error: String = ""
)
