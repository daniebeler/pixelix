package com.daniebeler.pixelix.domain.repository

import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    fun getHideSensitiveContent(): Flow<Boolean>

    suspend fun storeHideSensitiveContent(hideSensitiveContent: Boolean)

    fun getBaseUrlFromStorage(): Flow<String>

    fun getAccountId(): Flow<String>

    suspend fun storeClientId(clientId: String)
    suspend fun storeClientSecret(clientSecret: String)
    fun getClientSecretFromStorage(): Flow<String>
    suspend fun storeAccountId(accountId: String)
    fun getClientIdFromStorage(): Flow<String>

}