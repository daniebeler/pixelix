package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedSearches(
    val pastSearches: List<SavedSearchItem> = emptyList()
)

@Serializable
data class SavedSearchItem(
    val type: Type = Type.Search,
    val value: String,
    val avatar: String?,
    val accountId: String?
)

enum class Type {
    Account, Hashtag, Search
}