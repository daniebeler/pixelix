package com.daniebeler.pixelix.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: DataStore<Preferences>
) : StorageRepository {

    private var hideSensitiveContent = false

    override fun getHideSensitiveContent(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] ?: false
    }

    override suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] = hideSensitiveContent
        }
        this.hideSensitiveContent = hideSensitiveContent
    }

    override fun getBaseUrlFromStorage(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
    }
}