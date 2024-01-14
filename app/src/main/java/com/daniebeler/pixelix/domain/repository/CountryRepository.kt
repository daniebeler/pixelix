package com.daniebeler.pixelix.domain.repository

import android.content.Context
import android.net.Uri
import com.daniebeler.pixelix.common.Resource
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
import kotlinx.coroutines.flow.Flow
import java.net.URI

interface CountryRepository {

    fun doesAccessTokenExist(): Boolean

    suspend fun storeClientId(clientId: String)
    suspend fun storeBaseUrl(url: String)
    fun getClientIdFromStorage(): Flow<String>
    fun getBaseUrlFromStorage(): Flow<String>
    suspend fun storeClientSecret(clientSecret: String)
    fun getClientSecretFromStorage(): Flow<String>
    suspend fun storeAccessToken(accessToken: String)
    suspend fun storeAccountId(accountId: String)
    suspend fun getAccountId(): Flow<String>
    fun getAccessTokenFromStorage(): Flow<String>
    fun setBaseUrl(url: String)
    fun setAccessToken(token: String)
    fun getTrendingPosts(range: String): Flow<Resource<List<Post>>>
    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>

    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun getTrendingAccounts(): Flow<Resource<List<Account>>>
    fun getHashtagTimeline(hashtag: String, maxId: String = ""): Flow<Resource<List<Post>>>
    fun getLocalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getGlobalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getHomeTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getLikedPosts(maxId: String = ""): Flow<Resource<List<Post>>>
    fun getBookmarkedPosts(): Flow<Resource<List<Post>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>

    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>

    fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>>

    fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>>

    fun getAccount(accountId: String): Flow<Resource<Account?>>

    fun getInstance(): Flow<Resource<Instance>>

    fun followAccount(accountId: String): Flow<Resource<Relationship>>

    fun unfollowAccount(accountId: String): Flow<Resource<Relationship>>

    fun followHashtag(tagId: String): Flow<Resource<Tag>>

    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>

    fun likePost(postId: String): Flow<Resource<Post>>
    fun unlikePost(postId: String): Flow<Resource<Post>>

    fun bookmarkPost(postId: String): Flow<Resource<Post>>

    fun unBookmarkPost(postId: String): Flow<Resource<Post>>
    fun muteAccount(accountId: String): Flow<Resource<Relationship>>
    fun unMuteAccount(accountId: String): Flow<Resource<Relationship>>

    fun blockAccount(accountId: String): Flow<Resource<Relationship>>
    fun unblockAccount(accountId: String): Flow<Resource<Relationship>>

    fun getAccountsFollowers(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>>
    fun getAccountsFollowing(accountId: String, maxId: String = ""): Flow<Resource<List<Account>>>

    fun getMutedAccounts(): Flow<Resource<List<Account>>>
    fun getBlockedAccounts(): Flow<Resource<List<Account>>>

    fun getNotifications(maxNotificationId: String = ""): Flow<Resource<List<Notification>>>

    fun getPostsByAccountId(accountId: String, maxPostId: String = ""): Flow<Resource<List<Post>>>

    fun getPostById(postId: String): Flow<Resource<Post?>>

    fun search(searchText: String): Flow<Resource<Search>>

    fun uploadMedia(uri: Uri, context: Context): Flow<Resource<MediaAttachment>>

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}