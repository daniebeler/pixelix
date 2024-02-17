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

    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>

    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun getTrendingAccounts(): Flow<Resource<List<Account>>>


    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>

    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>

    fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>>

    fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>>



    fun getInstance(): Flow<Resource<Instance>>



    fun followHashtag(tagId: String): Flow<Resource<Tag>>

    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>







    fun getNotifications(maxNotificationId: String = ""): Flow<Resource<List<Notification>>>



    fun search(searchText: String): Flow<Resource<Search>>



    fun createReply(postId: String, content: String): Flow<Resource<Post>>

    suspend fun createApplication(): Application?

    fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): Flow<Resource<AccessToken>>

    fun verifyToken(token: String): Flow<Resource<Account>>
}