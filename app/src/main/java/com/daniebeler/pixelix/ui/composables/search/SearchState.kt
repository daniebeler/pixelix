package com.daniebeler.pixelix.ui.composables.search

import com.daniebeler.pixelix.domain.model.Search

data class SearchState(
    val isLoading: Boolean = false,
    val searchResult: Search? = null,
    val error: String = ""
)
