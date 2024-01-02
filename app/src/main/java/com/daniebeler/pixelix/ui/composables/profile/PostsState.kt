package com.daniebeler.pixelix.ui.composables.profile

import com.daniebeler.pixelix.domain.model.Post

data class PostsState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)
