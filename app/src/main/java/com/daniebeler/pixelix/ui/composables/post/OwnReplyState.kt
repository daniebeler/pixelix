package com.daniebeler.pixelix.ui.composables.post

import com.daniebeler.pixelix.domain.model.Post

data class OwnReplyState(
    val isLoading: Boolean = false,
    val reply: Post? = null,
    val error: String = ""
)
