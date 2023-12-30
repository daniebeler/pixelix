package com.daniebeler.pixels.ui.composables.followers

import com.daniebeler.pixels.domain.model.Account

data class FollowersState(
    val isLoading: Boolean = false,
    val followers: List<Account> = emptyList(),
    val error: String = ""
)
