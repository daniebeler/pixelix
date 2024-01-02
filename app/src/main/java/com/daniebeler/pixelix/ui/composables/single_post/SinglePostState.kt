package com.daniebeler.pixelix.ui.composables.single_post

import com.daniebeler.pixelix.domain.model.Post

data class SinglePostState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val error: String = ""
)
