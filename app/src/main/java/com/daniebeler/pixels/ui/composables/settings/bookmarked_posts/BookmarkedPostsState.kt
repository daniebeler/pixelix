package com.daniebeler.pixels.ui.composables.settings.bookmarked_posts

import com.daniebeler.pixels.domain.model.Post

data class BookmarkedPostsState(
    val isLoading: Boolean = false,
    val bookmarkedPosts: List<Post> = emptyList(),
    val error: String = ""
)
