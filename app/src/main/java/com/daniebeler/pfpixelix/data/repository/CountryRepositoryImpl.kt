package com.daniebeler.pfpixelix.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateReplyDto
import com.daniebeler.pfpixelix.data.remote.dto.InstanceDto
import com.daniebeler.pfpixelix.data.remote.dto.PlaceDto
import com.daniebeler.pfpixelix.data.remote.dto.PostContextDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.WellKnownDomainsDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.FediSoftwareDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.NodeInfoDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.WrapperDto
import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Application
import com.daniebeler.pfpixelix.domain.model.Instance
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.domain.model.Place
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.PostContext
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.model.WellKnownDomains
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediServer
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeInfo
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.execute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject


class CountryRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>,
    private val pixelfedApi: PixelfedApi
) : CountryRepository {
    override fun getAuthV1Token(): Flow<String> = userDataStorePreferences.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] ?: ""
    }

    override fun getAuthV1Baseurl(): Flow<String> =
        userDataStorePreferences.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
        }

    override suspend fun deleteAuthV1Data() {
        userDataStorePreferences.edit {
            it.remove(stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY))
            it.remove(stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY))
        }
    }

    override fun getTrendingAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getTrendingAccounts()
        )
    }


    override fun getReplies(postId: String): Flow<Resource<PostContext>> {
        return NetworkCall<PostContext, PostContextDto>().makeCall(
            pixelfedApi.getReplies(postId)
        )
    }


    override fun getNotifications(maxNotificationId: String): Flow<Resource<List<Notification>>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = if (maxNotificationId.isNotEmpty()) {
                    pixelfedApi.getNotifications(maxNotificationId).execute()
                } else {
                    pixelfedApi.getNotifications().execute()
                }

                val res = response.map { it.toModel() }
                emit(Resource.Success(res))
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


    override fun search(searchText: String, type: String?, limit: Int): Flow<Resource<Search>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getSearch(searchText, type, limit).execute().toModel()
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun searchLocations(searchText: String): Flow<Resource<List<Place>>> {
        return NetworkCall<Place, PlaceDto>().makeCallList(
            pixelfedApi.searchLocations(
                searchText
            )
        )
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
            pixelfedApi.createApplication().execute().toModel()
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

    override fun getSoftwareFromFediDB(slug: String): Flow<Resource<FediSoftware>> {
        return NetworkCall<FediSoftware, FediSoftwareDto>().makeCall(
            pixelfedApi.getSoftwareFromFediDB(slug)
        )
    }

    override fun getServerFromFediDB(slug: String): Flow<Resource<FediServer>> {
        return NetworkCall<FediServer, WrapperDto>().makeCall(
            pixelfedApi.getServerFromFediDB(slug)
        )
    }
}