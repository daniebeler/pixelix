package com.daniebeler.pfpixelix.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: DataStore<Preferences>
) : StorageRepository {

    private var hideSensitiveContent = false

    override fun getHideSensitiveContent(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] ?: true
    }

    override suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] =
                hideSensitiveContent
        }
        this.hideSensitiveContent = hideSensitiveContent
    }

    override fun getUseInAppBrowser(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.USE_IN_APP_BROWSER_DATASTORE_KEY)] ?: true
    }

    override suspend fun storeUseInAppBrowser(hideSensitiveContent: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.USE_IN_APP_BROWSER_DATASTORE_KEY)] =
                hideSensitiveContent
        }
    }

    override fun getBaseUrlFromStorage(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
    }

    override fun getAccountId(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.ACCOUNT_ID_DATASTORE_KEY)] ?: ""
    }

    override suspend fun storeClientId(clientId: String) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] = clientId
        }
    }

    override suspend fun storeAccountId(accountId: String) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.ACCOUNT_ID_DATASTORE_KEY)] = accountId
        }
    }

    override fun getClientIdFromStorage(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] ?: ""
    }

    override suspend fun storeVolume(volume: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.VOLUME_DATASTORE_KEY)] = volume
        }
    }

    override fun getStoreVolume(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.VOLUME_DATASTORE_KEY)] ?: true
    }

    override suspend fun storeTheme(theme: String) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.THEME_DATASTORE_KEY)] = theme
        }
    }

    override fun getStoreTheme(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.THEME_DATASTORE_KEY)] ?: "system"
    }


    override suspend fun storeClientSecret(clientSecret: String) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] = clientSecret
        }
    }

    override fun getClientSecretFromStorage(): Flow<String> = storage.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] ?: ""
    }
}