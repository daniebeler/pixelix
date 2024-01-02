package com.daniebeler.pixelix.ui.composables.settings.muted_accounts

import com.daniebeler.pixelix.domain.model.Account

data class MutedAccountsState(
    val isLoading: Boolean = false,
    val mutedAccounts: List<Account> = emptyList(),
    val error: String = ""
)
