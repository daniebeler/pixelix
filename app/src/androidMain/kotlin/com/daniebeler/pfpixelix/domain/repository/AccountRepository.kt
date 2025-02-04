package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Settings
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccount(accountId: String): Flow<Resource<Account>>
    fun getAccountByUsername(accountId: String): Flow<Resource<Account>>
    fun muteAccount(accountId: String): Flow<Resource<Relationship>>
    fun unMuteAccount(accountId: String): Flow<Resource<Relationship>>
    fun blockAccount(accountId: String): Flow<Resource<Relationship>>
    fun unblockAccount(accountId: String): Flow<Resource<Relationship>>
    fun getMutedAccounts(): Flow<Resource<List<Account>>>
    fun getBlockedAccounts(): Flow<Resource<List<Account>>>
    fun followAccount(accountId: String): Flow<Resource<Relationship>>
    fun unfollowAccount(accountId: String): Flow<Resource<Relationship>>
    fun getAccountsFollowers(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>>
    fun getAccountsFollowing(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>>
    fun getLikedBy(postId: String): Flow<Resource<List<Account>>>
    fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>>
    fun updateAccount(body: MultiPartFormDataContent): Flow<Resource<Account>>
    fun getAccountSettings(): Flow<Resource<Settings>>
}