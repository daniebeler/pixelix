package com.daniebeler.pixels.ui.composables.settings.blocked_accounts

import com.daniebeler.pixels.domain.model.Account

data class BlockedAccountsState(
    val isLoading: Boolean = false,
    val blockedAccounts: List<Account> = emptyList(),
    val error: String = ""
)
