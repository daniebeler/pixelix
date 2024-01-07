package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import com.daniebeler.pixelix.domain.model.Post

data class HashtagTimelineState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val hashtagTimeline: List<Post> = emptyList(),
    val error: String = ""
)
