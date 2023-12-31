package com.daniebeler.pixels.ui.composables.trending.trending_accounts

import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Tag

data class TrendingAccountsState(
    val isLoading: Boolean = false,
    val trendingAccounts: List<Account> = emptyList(),
    val error: String = ""
)
