package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import com.daniebeler.pfpixelix.domain.model.LoginData

data class CurrentAccountState (
    val isLoading: Boolean = false,
    val currentAccount: LoginData? = null,
    val error: String = ""
)