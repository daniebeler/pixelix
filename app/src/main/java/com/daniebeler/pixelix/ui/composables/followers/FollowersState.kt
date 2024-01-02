package com.daniebeler.pixelix.ui.composables.followers

import com.daniebeler.pixelix.domain.model.Account

data class FollowersState(
    val isLoading: Boolean = false,
    val followers: List<Account> = emptyList(),
    val error: String = ""
)
