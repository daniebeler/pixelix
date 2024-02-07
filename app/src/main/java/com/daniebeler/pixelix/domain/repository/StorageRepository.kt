package com.daniebeler.pixelix.domain.repository

import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    fun getHideSensitiveContent(): Flow<Boolean>

    suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean)

    fun getBaseUrlFromStorage(): Flow<String>

}