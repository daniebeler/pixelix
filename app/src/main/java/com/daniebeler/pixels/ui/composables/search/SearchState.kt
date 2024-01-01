package com.daniebeler.pixels.ui.composables.search

import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Search

data class SearchState(
    val isLoading: Boolean = false,
    val searchResult: Search? = null,
    val error: String = ""
)
