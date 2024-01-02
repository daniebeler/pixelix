package com.daniebeler.pixelix.ui.composables.post

data class BookmarkState(
    val isLoading: Boolean = false,
    val bookmarked: Boolean = false,
    val error: String = ""
)
