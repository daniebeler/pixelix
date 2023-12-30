package com.daniebeler.pixels.ui.composables.trending.posts

import com.daniebeler.pixels.domain.model.Post

data class TrendingPostsState(
    val isLoading: Boolean = false,
    val trendingPosts: List<Post> = emptyList(),
    val error: String = ""
)
