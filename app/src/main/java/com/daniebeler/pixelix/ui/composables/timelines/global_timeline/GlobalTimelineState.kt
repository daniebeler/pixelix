package com.daniebeler.pixelix.ui.composables.timelines.global_timeline

import com.daniebeler.pixelix.domain.model.Post

data class GlobalTimelineState(
    val isLoading: Boolean = false,
    val globalTimeline: List<Post> = emptyList(),
    val error: String = ""
)
