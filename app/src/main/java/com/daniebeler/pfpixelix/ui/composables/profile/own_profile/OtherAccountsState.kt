package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import com.daniebeler.pfpixelix.domain.model.LoginData

data class OtherAccountsState(
    val isLoading: Boolean = false,
    val otherAccounts: List<LoginData> = emptyList(),
    val error: String = ""
)
