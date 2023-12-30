package com.daniebeler.pixels.ui.composables.settings.followed_hashtags

import com.daniebeler.pixels.domain.model.Tag

data class FollowedHashtagsState(
    val isLoading: Boolean = false,
    val followedHashtags: List<Tag> = emptyList(),
    val error: String = ""
)
