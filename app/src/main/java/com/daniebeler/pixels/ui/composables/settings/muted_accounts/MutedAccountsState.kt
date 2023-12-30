package com.daniebeler.pixels.ui.composables.settings.muted_accounts

import com.daniebeler.pixels.domain.model.Account

data class MutedAccountsState(
    val isLoading: Boolean = false,
    val mutedAccounts: List<Account> = emptyList(),
    val error: String = ""
)
