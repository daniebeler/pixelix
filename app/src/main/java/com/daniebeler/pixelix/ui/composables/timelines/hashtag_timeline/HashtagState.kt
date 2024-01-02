package com.daniebeler.pixelix.ui.composables.timelines.hashtag_timeline

import com.daniebeler.pixelix.domain.model.Tag

data class HashtagState(
    val isLoading: Boolean = false,
    val hashtag: Tag? = null,
    val error: String = ""
)
