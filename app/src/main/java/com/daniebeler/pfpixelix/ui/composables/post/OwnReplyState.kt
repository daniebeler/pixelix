package com.daniebeler.pfpixelix.ui.composables.post

import com.daniebeler.pfpixelix.domain.model.Post

data class OwnReplyState(
    val isLoading: Boolean = false,
    val reply: Post? = null,
    val error: String = ""
)
