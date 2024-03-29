package com.daniebeler.pfpixelix.data.repository

import androidx.datastore.core.DataStore
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import com.daniebeler.pfpixelix.domain.model.SavedSearchType
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedSearchesRepositoryImpl @Inject constructor(private val dataStore: DataStore<SavedSearches>) :
    SavedSearchesRepository {

    override suspend fun addAccount(accountUsername: String, accountId: String, avatarUrl: String) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Account, value = accountUsername, accountId = accountId, avatar = avatarUrl))
    }

    override suspend fun addHashtag(hashtag: String) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Hashtag, value = hashtag, avatar = null, accountId = null))
    }

    override suspend fun addSearch(search: String) {
        addItem(SavedSearchItem(savedSearchType = SavedSearchType.Search, value = search, avatar = null, accountId = null))
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

    override suspend fun deleteElement(item: SavedSearchItem) {
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

    override suspend fun getSavedSearches(): Flow<SavedSearches> = dataStore.data
    override suspend fun clearSavedSearches() {
        try {
            dataStore.updateData { SavedSearches() }
        } catch (e: Exception) {
            println(e)
        }
    }

}