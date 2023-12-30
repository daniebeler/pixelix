package com.daniebeler.pixels.ui.composables.timelines.local_timeline

import com.daniebeler.pixels.domain.model.Post

data class LocalTimelineState(
    val isLoading: Boolean = false,
    val localTimeline: List<Post> = emptyList(),
    val error: String = ""
)
