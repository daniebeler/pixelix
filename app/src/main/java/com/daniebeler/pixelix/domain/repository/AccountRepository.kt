package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Relationship
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AccountRepository {
    fun getAccount(accountId: String): Flow<Resource<Account>>
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
}