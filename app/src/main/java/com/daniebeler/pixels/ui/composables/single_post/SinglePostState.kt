package com.daniebeler.pixels.ui.composables.single_post

import com.daniebeler.pixels.domain.model.Post

data class SinglePostState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val error: String = ""
)
