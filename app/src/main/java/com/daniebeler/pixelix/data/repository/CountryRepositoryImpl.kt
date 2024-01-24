package com.daniebeler.pixelix.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.remote.dto.AccountDto
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.data.remote.dto.InstanceDto
import com.daniebeler.pixelix.data.remote.dto.PostDto
import com.daniebeler.pixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pixelix.data.remote.dto.TagDto
import com.daniebeler.pixelix.domain.model.AccessToken
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Application
import com.daniebeler.pixelix.domain.model.Instance
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
import com.daniebeler.pixelix.domain.model.toReply
import com.daniebeler.pixelix.domain.model.toSearch
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.TimeUnit


private val Context.dataStore by preferencesDataStore("settings")

class CountryRepositoryImpl(context: Context) : CountryRepository {

    private val settingsDataStore = context.dataStore

    private var baseUrl = ""
    private var accessToken: String = ""

    private lateinit var pixelfedApi: PixelfedApi

    init {
        runBlocking {
            val accessTokenFromStorage = getAccessTokenFromStorage().first()
            if (accessTokenFromStorage.isNotEmpty()) {
                println("Bearer $accessTokenFromStorage")
                accessToken = "Bearer $accessTokenFromStorage"
            }
            val baseUrlFromStorage = getBaseUrlFromStorage().first()
            if (baseUrlFromStorage.isNotEmpty()) {
                baseUrl = baseUrlFromStorage
                pixelfedApi = buildPixelFedApi(false)
            }
        }
    }

    private fun buildPixelFedApi(imageUpload: Boolean): PixelfedApi {
        val mHttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val mOkHttpClient = if (imageUpload) {
            OkHttpClient.Builder().writeTimeout(25, TimeUnit.SECONDS)
                .addInterceptor(mHttpLoggingInterceptor).build()
        } else {
            OkHttpClient.Builder().addInterceptor(mHttpLoggingInterceptor).build()
        }

        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient).build()

        return retrofit.create(PixelfedApi::class.java)
    }

    override fun doesAccessTokenExist(): Boolean {
        return accessToken.isNotEmpty()
    }

    override suspend fun storeClientId(clientId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.CLIENT_ID_DATASTORE_KEY)] = clientId
        }
    }

    override suspend fun storeBaseUrl(url: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] = url
        }
        baseUrl = url
        pixelfedApi = buildPixelFedApi(false)
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

    override fun setBaseUrl(url: String) {
        baseUrl = url
    }

    override fun setAccessToken(token: String) {
        this.accessToken = token
    }

    override fun getTrendingPosts(range: String): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getTrendingPosts(range))
    }

    override fun getTrendingHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getTrendingHashtags(accessToken))
    }

    override fun getHashtag(hashtag: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.getHashtag(hashtag, accessToken))
    }

    override fun getTrendingAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getTrendingAccounts(
                accessToken
            )
        )
    }

    override fun getHashtagTimeline(hashtag: String, maxId: String): Flow<Resource<List<Post>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag, accessToken, maxId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag, accessToken
                )
            )
        }
    }

    override fun getLocalTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getLocalTimeline(
                    maxPostId, accessToken
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getLocalTimeline(accessToken))
        }
    }

    override fun getGlobalTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getGlobalTimeline(
                    maxPostId, accessToken
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getGlobalTimeline(accessToken))
        }
    }

    override fun getHomeTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHomeTimeline(
                    maxPostId, accessToken
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getHomeTimeline(accessToken))
        }
    }

    override fun getLikedPosts(maxId: String): Flow<Resource<List<Post>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getLikedPosts(accessToken, maxId))
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getLikedPosts(accessToken))
        }
    }

    override fun getBookmarkedPosts(): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getBookmarkedPosts(accessToken))
    }

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getFollowedHashtags(accessToken))
    }

    override fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getReplies(userid, postId).awaitResponse()
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

    override fun getAccount(accountId: String): Flow<Resource<Account>> {
        return NetworkCall<Account, AccountDto>().makeCall(
            pixelfedApi.getAccount(
                accountId, accessToken
            )
        )
    }

    override fun followAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.followAccount(
                accountId, accessToken
            )
        )
    }

    override fun unfollowAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unfollowAccount(
                accountId, accessToken
            )
        )
    }

    override fun followHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.followHashtag(tagId, accessToken))
    }

    override fun unfollowHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.unfollowHashtag(tagId, accessToken))
    }

    override fun likePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.likePost(postId, accessToken))
    }

    override fun unlikePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.unlikePost(postId, accessToken))
    }

    override fun bookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.bookmarkPost(postId, accessToken))
    }

    override fun unBookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(
            pixelfedApi.unbookmarkPost(
                postId, accessToken
            )
        )
    }

    override fun muteAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.muteAccount(
                accountId, accessToken
            )
        )
    }

    override fun unMuteAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unmuteAccount(
                accountId, accessToken
            )
        )
    }

    override fun blockAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.blockAccount(
                accountId, accessToken
            )
        )
    }

    override fun unblockAccount(accountId: String): Flow<Resource<Relationship>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCall(
            pixelfedApi.unblockAccount(
                accountId, accessToken
            )
        )
    }

    override fun getAccountsFollowers(
        accountId: String, maxId: String
    ): Flow<Resource<List<Account>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Account, AccountDto>().makeCallList(
                pixelfedApi.getAccountsFollowers(
                    accountId, accessToken, maxId
                )
            )
        } else {
            NetworkCall<Account, AccountDto>().makeCallList(
                pixelfedApi.getAccountsFollowers(
                    accountId, accessToken
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

    override fun getMutedAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getMutedAccounts(
                accessToken
            )
        )
    }

    override fun getBlockedAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getBlockedAccounts(
                accessToken
            )
        )
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
    ): Flow<Resource<List<Post>>> {
        return if (maxPostId.isEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId, accessToken
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId, accessToken, maxPostId
                )
            )
        }
    }

    override fun getLikedBy(postId: String): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getAccountsWhoLikedPost(
                accessToken,
                postId
            )
        )
    }

    override fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCallList(
            pixelfedApi.getRelationships(
                userIds, accessToken
            )
        )
    }

    override fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getMutalFollowers(
                userId, accessToken
            )
        )
    }

    override fun getPostById(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.getPostById(postId, accessToken))
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

    override fun getInstance(): Flow<Resource<Instance>> {
        return NetworkCall<Instance, InstanceDto>().makeCall(pixelfedApi.getInstance())
    }

    @SuppressLint("Recycle")
    override fun uploadMedia(
        uri: Uri,
        description: String,
        context: Context
    ): Flow<Resource<MediaAttachment>> = flow {
        try {
            emit(Resource.Loading())
            val pixelfedApi = buildPixelFedApi(true)
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileType = getMimeType(uri, context.contentResolver) ?: "image/*"
            val fileRequestBody =
                inputStream?.readBytes()?.toRequestBody(fileType.toMediaTypeOrNull())

            val file = File(uri.path!!)

            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("description", description)
                .addFormDataPart("file", file.name, fileRequestBody!!)

            if (fileType.take(5) != "image" || fileType == "image/gif") {
                val thumbnailBitmap = getThumbnail(uri, context)
                if (thumbnailBitmap != null) {
                    bitmapToBytes(thumbnailBitmap)?.let { builder.addFormDataPart("thumbnail", "thumbnail", it.toRequestBody()) }
                }
            }

            val requestBody: RequestBody = builder.build()
            val response = pixelfedApi.uploadMedia(
                accessToken,
                requestBody
            ).awaitResponse()
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

    fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun getThumbnail(uri: Uri, context: Context): Bitmap? {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions().frame(0))
                .submit()
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun getMimeType(uri: Uri, contentResolver: ContentResolver): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
        }
    }

    override fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.createPost(accessToken, createPostDto)
            if (response != null) {
                val res = response.body()!!.toPost()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            if (exception.message != null) {
                emit(Resource.Error(exception.message!!))
            }
            emit(Resource.Error("Unknown Error"))
        }
    }

    override fun deletePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(
            pixelfedApi.deletePost(
                accessToken, postId
            )
        )
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
            val response = pixelfedApi.verifyToken("Bearer $token").awaitResponse()
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