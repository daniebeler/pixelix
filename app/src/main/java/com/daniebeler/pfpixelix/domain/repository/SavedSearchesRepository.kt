package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import kotlinx.coroutines.flow.Flow

interface SavedSearchesRepository {
    suspend fun addAccount(accountUsername: String, account: Account)
    suspend fun addHashtag(hashtag: String)
    suspend fun addSearch(search: String)
    suspend fun deleteElement(item: SavedSearchItem)
    suspend fun getSavedSearches(): Flow<SavedSearches>
    suspend fun clearSavedSearches()
}