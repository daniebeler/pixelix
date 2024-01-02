package com.daniebeler.pixelix.ui.composables.settings.liked_posts

import com.daniebeler.pixelix.domain.model.Post

data class LikedPostsState(
    val isLoading: Boolean = false,
    val likedPosts: List<Post> = emptyList(),
    val error: String = ""
)
