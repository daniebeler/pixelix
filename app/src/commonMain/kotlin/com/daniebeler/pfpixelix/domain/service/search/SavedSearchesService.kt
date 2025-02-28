package com.daniebeler.pfpixelix.domain.service.search

import androidx.datastore.core.DataStore
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.model.SavedSearchType
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SavedSearchesService(
    private val dataStore: DataStore<SavedSearches>
) {
    suspend fun addAccount(accountUsername: String, account: Account) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Account, value = accountUsername, account = account))
    }

    suspend fun addHashtag(hashtag: String) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Hashtag, value = hashtag, account = null))
    }

    suspend fun addSearch(search: String) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Search, value = search, account = null))
    }

    private suspend fun addItem(item: SavedSearchItem) {
        try {
            dataStore.updateData { savedSearches ->
                if (savedSearches.pastSearches.any { it == item }) {
                    savedSearches.copy(
                        pastSearches = savedSearches.pastSearches.filterNot { it.value == item.value && it.savedSearchType == item.savedSearchType } + item
                    )
                } else {
                    savedSearches.copy(
                        pastSearches = savedSearches.pastSearches + item
                    )
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun deleteElement(item: SavedSearchItem) {
        try {
            dataStore.updateData { savedSearches ->
                savedSearches.copy(
                    pastSearches = savedSearches.pastSearches.filterNot { it == item }
                )
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun getSavedSearches(): Flow<SavedSearches> = dataStore.data
    suspend fun clearSavedSearches() {
        try {
            dataStore.updateData { SavedSearches() }
        } catch (e: Exception) {
            println(e)
        }
    }
}