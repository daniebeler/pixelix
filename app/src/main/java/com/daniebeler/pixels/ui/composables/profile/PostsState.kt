package com.daniebeler.pixels.ui.composables.profile

import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Post

data class PostsState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)
