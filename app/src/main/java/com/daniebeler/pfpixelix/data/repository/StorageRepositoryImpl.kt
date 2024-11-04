package com.daniebeler.pfpixelix.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.repository.StorageRepository
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: DataStore<Preferences>
) : StorageRepository {

    override fun getHideSensitiveContent(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] ?: true
    }

    override suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.SHOW_SENSITIVE_CONTENT_DATASTORE_KEY)] =
                hideSensitiveContent
        }
    }

    override fun getHideAltTextButton(): Flow<Boolean> = storage.data.map { preferences ->
        preferences[booleanPreferencesKey(Constants.SHOW_ALT_TEXT_BUTTON)] ?: false
    }

    override suspend fun storeHideAltTextButton(hideAltTextButton: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.SHOW_ALT_TEXT_BUTTON)] = hideAltTextButton
        }
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

    override suspend fun storeVolume(volume: Boolean) {
        storage.edit { preferences ->
            preferences[booleanPreferencesKey(Constants.VOLUME_DATASTORE_KEY)] = volume
        }
    }

    override fun getStoreVolume(): Flow<Boolean> = storage.data.map { preferences ->
        65

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

    override suspend fun storeView(view: ViewEnum) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.VIEW_DATASTORE_KEY)] = view.toString()
        }
    }

    override fun getStoredView(): Flow<ViewEnum> = storage.data.map { preferences ->
        ViewEnum.valueOf(
            preferences[stringPreferencesKey(Constants.VIEW_DATASTORE_KEY)] ?: "Grid"
        )
    }

    override suspend fun storeClientSecret(clientSecret: String) {
        storage.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] = clientSecret
        }
    }
}