package com.daniebeler.pixelix.ui.composables.post

data class LikeState(
    val isLoading: Boolean = false,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val error: String = ""
)
