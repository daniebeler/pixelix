package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun getHideSensitiveContent(): Flow<Boolean>
    suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean)

    fun getHideAltTextButton(): Flow<Boolean>
    suspend fun storeHideAltTextButton(hideAltTextButton: Boolean)

    fun getUseInAppBrowser(): Flow<Boolean>
    suspend fun storeUseInAppBrowser(hideSensitiveContent: Boolean)
    suspend fun storeClientSecret(clientSecret: String)
    suspend fun storeVolume(volume: Boolean)
    fun getStoreVolume(): Flow<Boolean>
    suspend fun storeView(view: ViewEnum)
    fun getStoredView(): Flow<ViewEnum>
    suspend fun storeTheme(theme: String)
    fun getStoreTheme(): Flow<String>
}