package com.daniebeler.pfpixelix.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.model.SavedSearches
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.model.Type
import com.daniebeler.pfpixelix.domain.repository.SavedSearchesRepository
import com.daniebeler.pfpixelix.utils.SavedSearchesSerializer
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedSearchesRepositoryImpl @Inject constructor(private val dataStore: DataStore<SavedSearches>) :
    SavedSearchesRepository {

    override suspend fun addAccount(accountUsername: String, accountId: String, avatarUrl: String) {
        addItem(SavedSearchItem(type = Type.Account, value = accountUsername, accountId = accountId, avatar = avatarUrl))
    }

    override suspend fun addHashtag(hashtag: String) {
        addItem(SavedSearchItem(type = Type.Hashtag, value = hashtag, avatar = null, accountId = null))
    }

    override suspend fun addSearch(search: String) {
        addItem(SavedSearchItem(type = Type.Search, value = search, avatar = null, accountId = null))
    }

    private suspend fun addItem(item: SavedSearchItem) {
        try {
            dataStore.updateData { savedSearches ->
                savedSearches.copy(
                    pastSearches = savedSearches.pastSearches + item
                )
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    override suspend fun getSavedSearches(): Flow<SavedSearches> = dataStore.data

}