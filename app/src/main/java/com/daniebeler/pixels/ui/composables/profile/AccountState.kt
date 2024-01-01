package com.daniebeler.pixels.ui.composables.profile

import com.daniebeler.pixels.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val error: String = ""
)