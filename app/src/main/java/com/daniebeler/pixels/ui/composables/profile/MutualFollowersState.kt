package com.daniebeler.pixels.ui.composables.profile

import com.daniebeler.pixels.domain.model.Account

data class MutualFollowersState(
    val isLoading: Boolean = false,
    val mutualFollowers: List<Account> = emptyList(),
    val error: String = ""
)
