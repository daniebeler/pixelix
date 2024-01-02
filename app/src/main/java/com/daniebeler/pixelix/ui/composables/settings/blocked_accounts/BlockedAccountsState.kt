package com.daniebeler.pixelix.ui.composables.settings.blocked_accounts

import com.daniebeler.pixelix.domain.model.Account

data class BlockedAccountsState(
    val isLoading: Boolean = false,
    val blockedAccounts: List<Account> = emptyList(),
    val error: String = ""
)
