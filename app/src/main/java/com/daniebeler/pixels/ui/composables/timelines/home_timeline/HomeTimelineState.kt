package com.daniebeler.pixels.ui.composables.timelines.home_timeline

import com.daniebeler.pixels.domain.model.Post

data class HomeTimelineState(
    val isLoading: Boolean = false,
    val homeTimeline: List<Post> = emptyList(),
    val error: String = ""
)
