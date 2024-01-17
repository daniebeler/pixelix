package com.daniebeler.pixelix.ui.composables.post

import com.daniebeler.pixelix.domain.model.Account

data class LikedByState(
    val isLoading: Boolean = false,
    val likedBy: List<Account> = emptyList(),
    val error: String = ""
)
