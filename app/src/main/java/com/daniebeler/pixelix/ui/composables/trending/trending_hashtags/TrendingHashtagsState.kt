package com.daniebeler.pixelix.ui.composables.trending.trending_hashtags

import com.daniebeler.pixelix.domain.model.Tag

data class TrendingHashtagsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val trendingHashtags: List<Tag> = emptyList(),
    val error: String = ""
)
