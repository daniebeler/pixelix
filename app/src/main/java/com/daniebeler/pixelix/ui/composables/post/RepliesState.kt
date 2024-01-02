package com.daniebeler.pixelix.ui.composables.post

import com.daniebeler.pixelix.domain.model.Reply

data class RepliesState(
    val isLoading: Boolean = false,
    val replies: List<Reply> = emptyList(),
    val error: String = ""
)
