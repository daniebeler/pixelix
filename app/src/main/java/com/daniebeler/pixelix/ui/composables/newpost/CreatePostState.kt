package com.daniebeler.pixelix.ui.composables.newpost

import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.Post

data class CreatePostState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val error: String = ""
)
