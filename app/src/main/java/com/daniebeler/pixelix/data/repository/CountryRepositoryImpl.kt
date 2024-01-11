package com.daniebeler.pixelix.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.domain.model.AccessToken
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Application
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.domain.model.Search
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.model.toAccessToken
import com.daniebeler.pixelix.domain.model.toAccount
import com.daniebeler.pixelix.domain.model.toApplication
import com.daniebeler.pixelix.domain.model.toMediaAttachment
import com.daniebeler.pixelix.domain.model.toNotification
import com.daniebeler.pixelix.domain.model.toPost
import com.daniebeler.pixelix.domain.model.toRelationship
import com.daniebeler.pixelix.domain.model.toReply
import com.daniebeler.pixelix.domain.model.toSearch
import com.daniebeler.pixelix.domain.model.toTag
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.URI

private val Context.dataStore by preferencesDataStore("settings")

class CountryRepositoryImpl(context: Context) : CountryRepository {

    private val settingsDataStore = context.dataStore

    private var BASE_URL = ""
    private var accessToken: String = ""

    private lateinit var pixelfedApi: PixelfedApi

    init {
        runBlocking {
            val accessTokenFromStorage = getAccessTokenFromStorage().first()
            if (accessTokenFromStorage.isNotEmpty()) {
                println("Bearer $accessTokenFromStorage")
                accessToken = "Bearer $accessTokenFromStorage"
            }
            val baseUrl = getBaseUrlFromStorage().first()
            if (baseUrl.isNotEmpty()) {
                BASE_URL = baseUrl
                buildPixelFedApi()
            }
        }
    }

    private fun buildPixelFedApi() {
        val mHttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient.Builder().addInterceptor(mHttpLoggingInterceptor).build()

        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient).build()

        pixelfedApi = retrofit.create(PixelfedApi::class.java)
    }

    override fun doesAccessTokenExist(): Boolean {
        return accessToken.isNotEmpty()
    }

    override suspend fun storeClientId(clientId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] = clientId
        }
    }

    override suspend fun storeBaseUrl(baseUrl: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] = baseUrl
        }
        BASE_URL = baseUrl
        buildPixelFedApi()
    }

    override fun getClientIdFromStorage(): Flow<String> =
        settingsDataStore.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] ?: ""
        }

    override fun getBaseUrlFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
    }


    override suspend fun storeClientSecret(clientSecret: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] = clientSecret
        }
    }

    override fun getClientSecretFromStorage(): Flow<String> =
        settingsDataStore.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] ?: ""
        }

    override suspend fun storeAccessToken(accessToken: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] = accessToken
        }
    }

    override suspend fun storeAccountId(accountId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.ACCOUNT_ID_DATASTORE_KEY)] = accountId
        }
    }

    override suspend fun getAccountId(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.ACCOUNT_ID_DATASTORE_KEY)] ?: ""
    }

    override fun getAccessTokenFromStorage(): Flow<String> =
        settingsDataStore.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] ?: ""
        }

    override fun setBaseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }

    override fun setAccessToken(token: String) {
        this.accessToken = token
    }

    override fun getTrendingPosts(range: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getTrendingPosts(range).awaitResponse()
            if (response.isSuccessful) {
                val result = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("an error occurred"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "an error occurred"))
        }
    }


    override fun getTrendingHashtags(): Flow<Resource<List<Tag>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getTrendingHashtags(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toTag() } ?: emptyList()
                res.forEach {
                    it.name = it.name.drop(1)
                }
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getHashtag(hashtag: String): Flow<Resource<Tag>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getHashtag(hashtag, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toTag()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getTrendingAccounts(): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getTrendingAccounts(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getHashtagTimeline(hashtag: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getHashtagTimeline(hashtag, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getLocalTimeline(maxPostId: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            var response = if (maxPostId.isNotEmpty()) {
                pixelfedApi.getLocalTimeline(maxPostId, accessToken).awaitResponse()
            } else {
                pixelfedApi.getLocalTimeline(accessToken).awaitResponse()
            }
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getGlobalTimeline(maxPostId: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            var response = if (maxPostId.isNotEmpty()) {
                pixelfedApi.getGlobalTimeline(maxPostId, accessToken).awaitResponse()
            } else {
                pixelfedApi.getGlobalTimeline(accessToken).awaitResponse()
            }
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getHomeTimeline(maxPostId: String): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            var response = if (maxPostId.isNotEmpty()) {
                pixelfedApi.getHomeTimeline(maxPostId, accessToken).awaitResponse()
            } else {
                pixelfedApi.getHomeTimeline(accessToken).awaitResponse()
            }

            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getLikedPosts(): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getLikedPosts(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getBookmarkedPosts(): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getBookmarkedPosts(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getFollowedHashtags(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toTag() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getReplies(userid: String, postid: String): Flow<Resource<List<Reply>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getReplies(userid, postid).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.data?.map { it.toReply() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun getAccount(accountId: String): Flow<Resource<Account?>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.toAccount()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun followAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.followAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun unfollowAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unfollowAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun followHashtag(tagId: String): Flow<Resource<Tag>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.followHashtag(tagId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toTag()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun unfollowHashtag(tagId: String): Flow<Resource<Tag>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unfollowHashtag(tagId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toTag()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun likePost(postId: String): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.likePost(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun unlikePost(postId: String): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unlikePost(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun bookmarkPost(postId: String): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.bookmarkPost(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun unbookmarkPost(postId: String): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unbookmarkPost(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun muteAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.muteAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun unMuteAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unmuteAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun blockAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.blockAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun unblockAccount(accountId: String): Flow<Resource<Relationship>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.unblockAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toRelationship()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun getAccountsFollowers(
        accountId: String, maxId: String
    ): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = if (maxId.isNotEmpty()) {
                pixelfedApi.getAccountsFollowers(accountId, accessToken, maxId).awaitResponse()
            } else {
                pixelfedApi.getAccountsFollowers(accountId, accessToken).awaitResponse()
            }

            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun getAccountsFollowing(
        accountId: String, maxId: String
    ): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = if (maxId.isNotEmpty()) {
                pixelfedApi.getAccountsFollowing(accountId, accessToken, maxId).awaitResponse()
            } else {
                pixelfedApi.getAccountsFollowing(accountId, accessToken).awaitResponse()
            }
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun getMutedAccounts(): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getMutedAccounts(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getBlockedAccounts(): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getBlockedAccounts(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }


    override fun getNotifications(maxNotificationId: String): Flow<Resource<List<Notification>>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = if (maxNotificationId.isNotEmpty()) {
                    pixelfedApi.getNotifications(accessToken, maxNotificationId).awaitResponse()
                } else {
                    pixelfedApi.getNotifications(accessToken).awaitResponse()
                }

                if (response.isSuccessful) {
                    val res = response.body()?.map { it.toNotification() } ?: emptyList()
                    emit(Resource.Success(res))
                } else {
                    emit(Resource.Error("Unknown Error"))
                }
            } catch (exception: Exception) {
                emit(Resource.Error(exception.message ?: "Unknown Error"))
            }
        }


    override fun getPostsByAccountId(
        accountId: String, maxPostId: String
    ): Flow<Resource<List<Post>>> = flow {
        try {
            emit(Resource.Loading())
            val response = if (maxPostId.isEmpty()) {
                pixelfedApi.getPostsByAccountId(accountId, accessToken).awaitResponse()
            } else {
                pixelfedApi.getPostsByAccountId(accountId, accessToken, maxPostId).awaitResponse()
            }

            if (response.isSuccessful) {
                val res = response.body()?.map { it.toPost() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Error"))
        }
    }

    override fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>> =
        flow {
            try {
                emit(Resource.Loading())
                val response = pixelfedApi.getRelationships(userIds, accessToken).awaitResponse()
                if (response.isSuccessful) {
                    val result = response.body()?.map { it.toRelationship() } ?: emptyList()
                    emit(Resource.Success(result))
                } else {
                    emit(Resource.Error("unknown error"))
                }
            } catch (exception: Exception) {
                emit(Resource.Error(exception.message ?: "unknown error"))
            }
        }

    override fun getMutalFollowers(userId: String): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getMutalFollowers(userId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.map { it.toAccount() } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getPostById(postId: String): Flow<Resource<Post?>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getPostById(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()?.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun search(searchText: String): Flow<Resource<Search>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getSearch(accessToken, searchText).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toSearch()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Unknown Error"))
        }
    }

    @SuppressLint("Recycle")
    override fun uploadMedia(uri: Uri, context: Context): Flow<Resource<MediaAttachment>> = flow {
        try {
            emit(Resource.Loading())
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileRequestBody =
                inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
            val file = File(uri.path!!)
            val multipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file", file.name, fileRequestBody!!
            )
            val response = pixelfedApi.uploadMedia(accessToken, multipart).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toMediaAttachment()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message!!))
        }
    }


// Auth

    override suspend fun createApplication(): Application? {
        return try {
            val response = pixelfedApi.createApplication().awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toApplication()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): AccessToken? {
        return try {
            val response = pixelfedApi.obtainToken(clientId, clientSecret, code).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toAccessToken()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun verifyToken(token: String): Account? {
        return try {
            val response = pixelfedApi.verifyToken("Bearer " + token).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toAccount()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

}