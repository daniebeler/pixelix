package com.daniebeler.pixelix.domain.repository

import android.content.Context
import android.net.Uri
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
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
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun doesAccessTokenExist(): Boolean


    suspend fun storeBaseUrl(url: String)

    fun getBaseUrlFromStorage(): Flow<String>

    suspend fun storeAccessToken(accessToken: String)

    fun getAccessTokenFromStorage(): Flow<String>
    fun setAccessToken(token: String)
    fun getTrendingPosts(range: String): Flow<Resource<List<Post>>>
    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>

    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun getTrendingAccounts(): Flow<Resource<List<Account>>>

    fun getLikedPosts(maxId: String = ""): Flow<Resource<LikedPostsWithNext>>
    fun getBookmarkedPosts(): Flow<Resource<List<Post>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>

    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>

    fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>>

    fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>>



    fun getInstance(): Flow<Resource<Instance>>



    fun followHashtag(tagId: String): Flow<Resource<Tag>>

    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>

    fun likePost(postId: String): Flow<Resource<Post>>
    fun unlikePost(postId: String): Flow<Resource<Post>>

    fun bookmarkPost(postId: String): Flow<Resource<Post>>

    fun unBookmarkPost(postId: String): Flow<Resource<Post>>





    fun getNotifications(maxNotificationId: String = ""): Flow<Resource<List<Notification>>>

    fun getPostsByAccountId(accountId: String, maxPostId: String = ""): Flow<Resource<List<Post>>>



    fun getPostById(postId: String): Flow<Resource<Post>>

    fun search(searchText: String): Flow<Resource<Search>>

    fun uploadMedia(
        uri: Uri,
        description: String,
        context: Context,
        mediaAttachmentConfiguration: MediaAttachmentConfiguration
    ): Flow<Resource<MediaAttachment>>

    fun updateMedia(id: String, description: String): Flow<Resource<MediaAttachment>>
    fun createPost(createPostDto: CreatePostDto): Flow<Resource<Post>>
    fun deletePost(postId: String): Flow<Resource<Post>>

    fun createReply(postId: String, content: String): Flow<Resource<Post>>

    suspend fun createApplication(): Application?

    fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): Flow<Resource<AccessToken>>

    fun verifyToken(token: String): Flow<Resource<Account>>
}