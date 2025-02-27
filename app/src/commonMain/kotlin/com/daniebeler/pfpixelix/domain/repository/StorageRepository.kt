package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun storeVolume(volume: Boolean)
    fun getStoreVolume(): Flow<Boolean>
    suspend fun storeView(view: ViewEnum)
    fun getStoredView(): Flow<ViewEnum>
}