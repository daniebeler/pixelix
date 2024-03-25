package com.daniebeler.pfpixelix.ui.composables.post.reply

import com.daniebeler.pfpixelix.domain.model.Reply

data class RepliesState(
    val isLoading: Boolean = false,
    val replies: List<Reply> = emptyList(),
    val error: String = ""
)
