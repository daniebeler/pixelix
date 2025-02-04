package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.SettingsDto
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Settings
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.execute
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : AccountRepository {

    override fun getAccount(accountId: String): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.getAccount(
                accountId
            )
        )
    }

    override fun getAccountByUsername(username: String): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.getAccountByUsername(
                username
            )
        )
    }

    override fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getMutalFollowers(
                userId
            )
        )
    }

    override fun updateAccount(body: MultiPartFormDataContent): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.updateAccount(
                body
            )
        )
    }

    override fun getAccountSettings(): Flow<Resource<Settings>> {
        return NetworkCall<Settings, SettingsDto>().makeCall(
            pixelfedApi.getSettings()
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
            pixelfedApi.getBlockedAccounts()
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
                pixelfedApi.getAccountsFollowing(accountId, maxId).execute()
            } else {
                pixelfedApi.getAccountsFollowing(accountId).execute()
            }
                val res = response.map { it.toModel() }
                emit(Resource.Success(res))
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