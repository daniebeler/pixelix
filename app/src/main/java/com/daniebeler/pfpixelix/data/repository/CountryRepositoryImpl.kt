package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateReplyDto
import com.daniebeler.pfpixelix.data.remote.dto.InstanceDto
import com.daniebeler.pfpixelix.data.remote.dto.NodeInfoDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.WellKnownDomainsDto
import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Application
import com.daniebeler.pfpixelix.domain.model.Instance
import com.daniebeler.pfpixelix.domain.model.NodeInfo
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Reply
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.model.WellKnownDomains
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject


class CountryRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : CountryRepository {
    override fun getTrendingAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getTrendingAccounts()
        )
    }


    override fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getReplies(userid, postId).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.data?.map { it.toModel() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }


    override fun getNotifications(maxNotificationId: String): Flow<Resource<List<Notification>>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = if (maxNotificationId.isNotEmpty()) {
                    pixelfedApi.getNotifications(maxNotificationId).awaitResponse()
                } else {
                    pixelfedApi.getNotifications().awaitResponse()
                }

                if (response.isSuccessful) {
                    val res = response.body()?.map { it.toModel() } ?: emptyList()
                    emit(Resource.Success(res))
                } else {
                    emit(Resource.Error("Unknown Error"))
                }
            } catch (exception: Exception) {
                emit(Resource.Error(exception.message ?: "Unknown Error"))
            }
        }


    override fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCallList(
            pixelfedApi.getRelationships(
                userIds
            )
        )
    }


    override fun search(searchText: String, type: String?): Flow<Resource<Search>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getSearch(searchText, type).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toModel()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun getInstance(): Flow<Resource<Instance>> {
        return NetworkCall<Instance, InstanceDto>().makeCall(pixelfedApi.getInstance())
    }


    override fun createReply(postId: String, content: String): Flow<Resource<Post>> {
        val dto = CreateReplyDto(status = content, in_reply_to_id = postId)
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.createReply(dto))
    }

// Auth

    override suspend fun createApplication(): Application? {
        return try {
            val response = pixelfedApi.createApplication().awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toModel()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): Flow<Resource<AccessToken>> {
        return NetworkCall<AccessToken, AccessTokenDto>().makeCall(
            pixelfedApi.obtainToken(clientId, clientSecret, code)
        )
    }

    override fun verifyToken(token: String): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.verifyToken()
        )
    }

    override fun getWellKnownDomains(domain: String): Flow<Resource<WellKnownDomains>> {
        return NetworkCall<WellKnownDomains, WellKnownDomainsDto>().makeCall(
            pixelfedApi.getWellKnownDomains(domain)
        )
    }

    override fun getNodeInfo(domain: String): Flow<Resource<NodeInfo>> {
        return NetworkCall<NodeInfo, NodeInfoDto>().makeCall(
            pixelfedApi.getNodeInfo(domain)
        )
    }

}