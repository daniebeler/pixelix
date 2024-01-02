package com.daniebeler.pixelix.ui.composables.profile

import com.daniebeler.pixelix.domain.model.Account

data class MutualFollowersState(
    val isLoading: Boolean = false,
    val mutualFollowers: List<Account> = emptyList(),
    val error: String = ""
)
