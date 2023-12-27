package com.daniebeler.pixels.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.daniebeler.pixels.common.Constants
import com.daniebeler.pixels.data.remote.PixelfedApi
import com.daniebeler.pixels.domain.model.AccessToken
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Relationship
import com.daniebeler.pixels.domain.model.Reply
import com.daniebeler.pixels.domain.model.Tag
import com.daniebeler.pixels.domain.model.toAccessToken
import com.daniebeler.pixels.domain.model.toAccount
import com.daniebeler.pixels.domain.model.toApplication
import com.daniebeler.pixels.domain.model.toNotification
import com.daniebeler.pixels.domain.model.toPost
import com.daniebeler.pixels.domain.model.toRelationship
import com.daniebeler.pixels.domain.model.toReply
import com.daniebeler.pixels.domain.model.toTag
import com.daniebeler.pixels.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore by preferencesDataStore("settings")

class CountryRepositoryImpl(context: Context): CountryRepository {

    private val settingsDataStore = context.dataStore

    private var BASE_URL = "https://pixelfed.social/"
    private var accessToken: String = ""

    private val pixelfedApi: PixelfedApi

    init {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()

        pixelfedApi = retrofit.create(PixelfedApi::class.java)

        runBlocking {
            accessToken = "Bearer " + getAccessTokenFromStorage().first()
        }
    }

    override fun doesAccessTokenExist(): Boolean {
        return accessToken.isNotEmpty()
    }

    override suspend fun storeClientId(clientId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] = clientId
        }
    }

    override fun getClientIdFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] ?: ""
    }

    override suspend fun storeClientSecret(clientSecret: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] = clientSecret
        }
    }

    override fun getClientSecretFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.CLIENT_SECRET_DATASTORE_KEY)] ?: ""
    }

    override suspend fun storeAccessToken(accessToken: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] = accessToken
        }
    }

    override fun getAccessTokenFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] ?: ""
    }

    override fun setBaseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }

    override fun setAccessToken(token: String) {
        this.accessToken = token
    }

    override suspend fun getTrendingPosts(range: String): List<Post> {
        return try {
            val response = pixelfedApi.getTrendingPosts(range).awaitResponse()
            if (response.isSuccessful) {
                println("success")
                println(response.body())
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getTrendingHashtags(): List<Tag> {
        return try {
            val response = pixelfedApi.getTrendingHashtags(accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toTag() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getHashtagTimeline(hashtag: String): List<Post> {
        return try {
            val response = pixelfedApi.getHashtagTimeline(hashtag, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getLocalTimeline(): List<Post> {
        return try {
            val response = pixelfedApi.getLocalTimeline(accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getHomeTimeline(): List<Post> {
        return try {
            val response = pixelfedApi.getHomeTimeline(accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getHomeTimeline(maxPostId: String): List<Post> {
        return try {
            val response = pixelfedApi.getHomeTimeline(maxPostId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getReplies(userid: String, postid: String): List<Reply> {
        return try {
            val response = pixelfedApi.getReplies(userid, postid).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.data?.map { it.toReply() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getAccount(accountId: String): Account? {
        return try {
            val response = pixelfedApi.getAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toAccount()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun followAccount(accountId: String): Relationship? {
        return try {
            val response = pixelfedApi.followAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toRelationship()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun unfollowAccount(accountId: String): Relationship? {
        return try {
            val response = pixelfedApi.unfollowAccount(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toRelationship()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun getNotifications(): List<Notification> {
        return try {
            val response = pixelfedApi.getNotifications(accessToken).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body() ?: emptyList()
                res.map { it.toNotification() }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getPostsByAccountId(accountId: String): List<Post> {
        return try {
            val response = pixelfedApi.getPostsByAccountId(accountId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }


    override suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post> {
        return try {
            val response = pixelfedApi.getPostsByAccountId(accountId, accessToken, maxPostId).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toPost() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getRelationships(userId: String): List<Relationship> {
        return try {
            val response = pixelfedApi.getRelationships(userId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toRelationship() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getMutalFollowers(userId: String): List<Account> {
        return try {
            val response = pixelfedApi.getMutalFollowers(userId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.map { it.toAccount() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun getPostById(postId: String): Post? {
        return try {
            val response = pixelfedApi.getPostById(postId, accessToken).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.toPost()
            } else {
                null
            }
        } catch (exception: Exception) {
            null
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

    override suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken? {
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