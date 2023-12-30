package com.daniebeler.pixels.ui.composables.timelines.hashtag_timeline

import com.daniebeler.pixels.domain.model.Post

data class HashtagTimelineState(
    val isLoading: Boolean = false,
    val hashtagTimeline: List<Post> = emptyList(),
    val error: String = ""
)
