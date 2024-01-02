package com.daniebeler.pixelix.ui.composables.timelines.local_timeline

import com.daniebeler.pixelix.domain.model.Post

data class LocalTimelineState(
    val isLoading: Boolean = false,
    val localTimeline: List<Post> = emptyList(),
    val error: String = ""
)
