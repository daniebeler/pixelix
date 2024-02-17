package com.daniebeler.pixelix.data.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.remote.dto.AccountDto
import com.daniebeler.pixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.repository.AccountRepository
import com.daniebeler.pixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
): AccountRepository {
    override fun getAccount(accountId: String): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.getAccount(
                accountId
            )
        )
    }

    override fun followAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.followAccount(
                accountId
            )
        )
    }

    override fun unfollowAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unfollowAccount(
                accountId
            )
        )
    }

    override fun muteAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.muteAccount(
                accountId
            )
        )
    }

    override fun unMuteAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unmuteAccount(
                accountId
            )
        )
    }

    override fun blockAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.blockAccount(
                accountId
            )
        )
    }

    override fun unblockAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unblockAccount(
                accountId
            )
        )
    }

    override fun getMutedAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getMutedAccounts()
        )
    }

    override fun getBlockedAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getBlockedAccounts( )
        )
    }

    override fun getAccountsFollowers(
        accountId: String, maxId: String
    ): Flow<Resource<List<Account>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Account, AccountDto>().makeCallList(
                pixelfedApi.getAccountsFollowers(
                    accountId, maxId
                )
            )
        } else {
            NetworkCall<Account, AccountDto>().makeCallList(
                pixelfedApi.getAccountsFollowers(
                    accountId
                )
            )
        }
    }

    override fun getAccountsFollowing(
        accountId: String, maxId: String
    ): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = if (maxId.isNotEmpty()) {
                pixelfedApi.getAccountsFollowing(accountId, maxId).awaitResponse()
            } else {
                pixelfedApi.getAccountsFollowing(accountId).awaitResponse()
            }
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toModel() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun getLikedBy(postId: String): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getAccountsWhoLikedPost(
                postId
            )
        )
    }
}