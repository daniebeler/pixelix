package com.daniebeler.pixelix.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daniebeler.pixelix.common.Constants
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pixelix.data.remote.dto.AccountDto
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.data.remote.dto.CreateReplyDto
import com.daniebeler.pixelix.data.remote.dto.InstanceDto
import com.daniebeler.pixelix.data.remote.dto.MediaAttachmentDto
import com.daniebeler.pixelix.data.remote.dto.PostDto
import com.daniebeler.pixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pixelix.data.remote.dto.TagDto
import com.daniebeler.pixelix.di.HostSelectionInterceptorInterface
import com.daniebeler.pixelix.domain.model.AccessToken
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Application
import com.daniebeler.pixelix.domain.model.Instance
import com.daniebeler.pixelix.domain.model.LikedPostsWithNext
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.model.MediaAttachmentConfiguration
import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.domain.model.Search
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.utils.GetFile
import com.daniebeler.pixelix.utils.MimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class CountryRepositoryImpl @Inject constructor(
    private val userDataStorePreferences: DataStore<Preferences>,
    private val hostSelectionInterceptor: HostSelectionInterceptorInterface,
    private val pixelfedApi: PixelfedApi
) : CountryRepository {

    private var accessToken: String = ""


    init {
        runBlocking {
            val accessTokenFromStorage = getAccessTokenFromStorage().first()
            if (accessTokenFromStorage.isNotEmpty()) {
                accessToken = "Bearer $accessTokenFromStorage"
                hostSelectionInterceptor.setToken(accessTokenFromStorage)
            }
            val baseUrlFromStorage = getBaseUrlFromStorage().first()
            if (baseUrlFromStorage.isNotEmpty()) {

                println("fief: " + baseUrlFromStorage)
                hostSelectionInterceptor.setHost(baseUrlFromStorage.replace("https://", ""))

            }
        }
    }


    override fun doesAccessTokenExist(): Boolean {
        return accessToken.isNotEmpty()
    }

    override suspend fun storeBaseUrl(url: String) {
        val host = url.replace("https://", "")
        userDataStorePreferences.edit { preferences ->
            preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] = host
        }
        hostSelectionInterceptor.setHost(host)
    }



    override fun getBaseUrlFromStorage(): Flow<String> =
        userDataStorePreferences.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.BASE_URL_DATASTORE_KEY)] ?: ""
        }



    override suspend fun storeAccessToken(accessToken: String) {
        this.accessToken = "Bearer $accessToken"
        userDataStorePreferences.edit { preferences ->
            preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] = accessToken
        }
        this.accessToken = accessToken
    }



    override fun getAccessTokenFromStorage(): Flow<String> =
        userDataStorePreferences.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.ACCESS_TOKEN_DATASTORE_KEY)] ?: ""
        }

    override fun setAccessToken(token: String) {
        this.accessToken = token
    }

    override fun getTrendingPosts(range: String): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getTrendingPosts(range))
    }

    override fun getTrendingHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getTrendingHashtags())
    }

    override fun getHashtag(hashtag: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.getHashtag(hashtag))
    }

    override fun getTrendingAccounts(): Flow<Resource<List<Account>>> {
        return NetworkCall<Account, AccountDto>().makeCallList(
            pixelfedApi.getTrendingAccounts()
        )
    }

    override fun getLikedPosts(maxId: String): Flow<Resource<LikedPostsWithNext>> = flow {
        try {
            emit(Resource.Loading())
            val response = if (maxId.isNotBlank()) {
                pixelfedApi.getLikedPosts(maxId).awaitResponse()
            } else {
                pixelfedApi.getLikedPosts().awaitResponse()
            }

            if (response.isSuccessful) {

                val linkHeader = response.headers()["link"] ?: ""

                val onlyLink =
                    linkHeader.substringAfter("rel=\"next\",<", "").substringBefore(">", "")

                val nextLimit = onlyLink.substringAfter("limit=", "").substringBefore("&", "")
                val nextMinId = onlyLink.substringAfter("min_id=", "")

                val res = response.body()?.map { it.toModel() } ?: emptyList()

                val result = LikedPostsWithNext(res, nextMinId)
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun getBookmarkedPosts(): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getBookmarkedPosts())
    }

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getFollowedHashtags())
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



    override fun followHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.followHashtag(tagId))
    }

    override fun unfollowHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.unfollowHashtag(tagId))
    }

    override fun likePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.likePost(postId))
    }

    override fun unlikePost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.unlikePost(postId))
    }

    override fun bookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.bookmarkPost(postId))
    }

    override fun unBookmarkPost(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(
            pixelfedApi.unbookmarkPost(
                postId
            )
        )
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


    override fun getPostsByAccountId(
        accountId: String, maxPostId: String
    ): Flow<Resource<List<Post>>> {
        return if (maxPostId.isEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getPostsByAccountId(
                    accountId, maxPostId
                )
            )
        }
    }



    override fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>> {
        return NetworkCall<Relationship, RelationshipDto>().makeCallList(
            pixelfedApi.getRelationships(
                userIds
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

    override fun getPostById(postId: String): Flow<Resource<Post>> {
        return NetworkCall<Post, PostDto>().makeCall(pixelfedApi.getPostById(postId))
    }

    override fun search(searchText: String): Flow<Resource<Search>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.getSearch(searchText).awaitResponse()
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

    @SuppressLint("Recycle")
    override fun uploadMedia(
        uri: Uri,
        description: String,
        context: Context,
        mediaAttachmentConfiguration: MediaAttachmentConfiguration
    ): Flow<Resource<MediaAttachment>> = flow {
        try {
            emit(Resource.Loading())

            //val pixelfedApi = buildPixelFedApi(true)

            val fileType = MimeType().getMimeType(uri, context.contentResolver) ?: "image/*"

            val inputStream = context.contentResolver.openInputStream(uri)
            val fileRequestBody =
                inputStream?.readBytes()?.toRequestBody(fileType.toMediaTypeOrNull())

            val file = GetFile().getFile(uri, context) ?: return@flow

            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("description", description)
                .addFormDataPart("file", file.name, fileRequestBody!!)

            if (fileType.take(5) != "image" || fileType == "image/gif") {
                val thumbnailBitmap = getThumbnail(uri, context)
                if (thumbnailBitmap != null) {
                    bitmapToBytes(thumbnailBitmap)?.let {
                        builder.addFormDataPart(
                            "thumbnail", "thumbnail", it.toRequestBody()
                        )
                    }
                }
            }

            val requestBody: RequestBody = builder.build()
            val response = pixelfedApi.uploadMedia(requestBody
            ).awaitResponse()
            if (response.isSuccessful) {
                val res = response.body()!!.toModel()
                emit(Resource.Success(res))
            } else {
                emit(Resource.Error("Unknown Error"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message!!))
        }
    }

    private fun bitmapToBytes(photo: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private suspend fun getThumbnail(uri: Uri, context: Context): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                Glide.with(context).asBitmap().load(uri).apply(RequestOptions().frame(0)).submit()
                    .get()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun updateMedia(id: String, description: String): Flow<Resource<MediaAttachment>> {
        return NetworkCall<MediaAttachment, MediaAttachmentDto>().makeCall(
            pixelfedApi.updateMedia(
                id, description
            )
        )
    }

    override fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.createPost(createPostDto)
            if (response != null) {
                val res = response.body()!!.toModel()
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
            pixelfedApi.deletePost(postId
            )
        )
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
            pixelfedApi.verifyToken("Bearer $token")
        )
    }

}