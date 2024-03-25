package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedSearches(
    val pastSearches: List<SavedSearchItem> = emptyList()
)

@Serializable
data class SavedSearchItem(
    val savedSearchType: SavedSearchType = SavedSearchType.Search,
    val value: String,
    val avatar: String?,
    val accountId: String?
)

enum class SavedSearchType {
    Account, Hashtag, Search
}