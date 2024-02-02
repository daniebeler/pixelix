package com.daniebeler.pixelix.ui.composables.trending.trending_posts

import com.daniebeler.pixelix.domain.model.Post

data class TrendingPostsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val trendingPosts: List<Post> = emptyList(),
    val error: String = ""
)
