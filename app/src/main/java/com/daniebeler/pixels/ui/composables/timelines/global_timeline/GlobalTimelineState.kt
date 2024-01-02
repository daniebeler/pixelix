package com.daniebeler.pixels.ui.composables.timelines.global_timeline

import com.daniebeler.pixels.domain.model.Post

data class GlobalTimelineState(
    val isLoading: Boolean = false,
    val globalTimeline: List<Post> = emptyList(),
    val error: String = ""
)
