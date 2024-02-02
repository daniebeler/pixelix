package com.daniebeler.pixelix.ui.composables.settings.bookmarked_posts

import com.daniebeler.pixelix.domain.model.Post

data class BookmarkedPostsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val bookmarkedPosts: List<Post> = emptyList(),
    val error: String = ""
)
