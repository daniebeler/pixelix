package com.daniebeler.pfpixelix.ui.composables.post

data class BookmarkState(
    val isLoading: Boolean = false,
    val bookmarked: Boolean = false,
    val error: String = ""
)
