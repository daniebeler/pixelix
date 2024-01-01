package com.daniebeler.pixels.ui.composables.timelines.home_timeline

import com.daniebeler.pixels.domain.model.Post

data class HomeTimelineState(
    var isLoading: Boolean = false,
    var homeTimeline: List<Post> = emptyList(),
    var error: String = ""
)
