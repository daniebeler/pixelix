package com.daniebeler.pixelix.ui.composables.settings.followed_hashtags

import com.daniebeler.pixelix.domain.model.Tag

data class FollowedHashtagsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val followedHashtags: List<Tag> = emptyList(),
    val error: String = ""
)
