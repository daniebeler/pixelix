package com.daniebeler.pixels.ui.composables.followers

import com.daniebeler.pixels.domain.model.Account

data class FollowingState(
    val isLoading: Boolean = false,
    val following: List<Account> = emptyList(),
    val error: String = ""
)
