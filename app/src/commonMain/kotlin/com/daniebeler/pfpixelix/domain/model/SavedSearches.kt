package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class SavedSearches(
    val pastSearches: List<SavedSearchItem> = emptyList()
)

@Serializable
data class SavedSearchItem(
    val savedSearchType: SavedSearchType = SavedSearchType.Search,
    val value: String,
    val account: Account?
)

enum class SavedSearchType {
    Account, Hashtag, Search
}