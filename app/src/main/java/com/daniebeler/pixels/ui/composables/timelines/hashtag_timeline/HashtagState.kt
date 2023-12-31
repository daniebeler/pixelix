package com.daniebeler.pixels.ui.composables.timelines.hashtag_timeline

import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Tag

data class HashtagState(
    val isLoading: Boolean = false,
    val hashtag: Tag? = null,
    val error: String = ""
)
