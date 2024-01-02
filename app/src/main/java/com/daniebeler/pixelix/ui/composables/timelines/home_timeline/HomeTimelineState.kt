package com.daniebeler.pixelix.ui.composables.timelines.home_timeline

import com.daniebeler.pixelix.domain.model.Post

data class HomeTimelineState(
    var isLoading: Boolean = false,
    var homeTimeline: List<Post> = emptyList(),
    var error: String = ""
)
