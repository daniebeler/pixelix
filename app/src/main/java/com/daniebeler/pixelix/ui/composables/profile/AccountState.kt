package com.daniebeler.pixelix.ui.composables.profile

import com.daniebeler.pixelix.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val error: String = ""
)