package com.daniebeler.pixels.ui.composables.trending.trending_hashtags

import com.daniebeler.pixels.domain.model.Tag

data class TrendingHashtagsState(
    val isLoading: Boolean = false,
    val trendingHashtags: List<Tag> = emptyList(),
    val error: String = ""
)
