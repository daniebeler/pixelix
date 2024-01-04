package com.daniebeler.pixelix.ui.composables.post

import com.daniebeler.pixelix.domain.model.Reply

data class LikeState(
    val isLoading: Boolean = false,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val error: String = ""
)
