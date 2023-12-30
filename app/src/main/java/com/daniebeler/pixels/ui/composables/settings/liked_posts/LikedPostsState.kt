package com.daniebeler.pixels.ui.composables.settings.liked_posts

import com.daniebeler.pixels.domain.model.Post

data class LikedPostsState(
    val isLoading: Boolean = false,
    val likedPosts: List<Post> = emptyList(),
    val error: String = ""
)
