package com.daniebeler.pixelix.ui.composables.followers

import com.daniebeler.pixelix.domain.model.Account

data class FollowingState(
    val isLoading: Boolean = false,
    val following: List<Account> = emptyList(),
    val error: String = ""
)
