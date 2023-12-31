package com.daniebeler.pixels.domain.repository

import android.net.http.UrlRequest.Status
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.AccessToken
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Relationship
import com.daniebeler.pixels.domain.model.Reply
import com.daniebeler.pixels.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun doesAccessTokenExist(): Boolean

    suspend fun storeClientId(clientId: String)
    suspend fun storeBaseUrl(baseUrl: String)
    fun getClientIdFromStorage(): Flow<String>
    fun getBaseUrlFromStorage() : Flow<String>
    suspend fun storeClientSecret(clientSecret: String)
    fun getClientSecretFromStorage(): Flow<String>
    suspend fun storeAccessToken(accessToken: String)
    suspend fun storeAccountId(accountId: String)
    suspend fun getAccountId(): Flow<String>
    fun getAccessTokenFromStorage(): Flow<String>
    fun setBaseUrl(baseUrl: String)
    fun setAccessToken(token: String)
    fun getTrendingPosts(range: String): Flow<Resource<List<Post>>>
    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>

    fun getHashtagTimeline(hashtag: String): Flow<Resource<List<Post>>>

    fun getLocalTimeline(): Flow<Resource<List<Post>>>

    fun getHomeTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getLikedPosts(): Flow<Resource<List<Post>>>
    fun getBookmarkedPosts(): Flow<Resource<List<Post>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>

    suspend fun getRelationships(userId: String): List<Relationship>

    suspend fun getMutalFollowers(userId: String): List<Account>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    suspend fun getAccount(accountId: String): Account?

    suspend fun followAccount(accountId: String): Relationship?

    suspend fun unfollowAccount(accountId: String): Relationship?
    suspend fun likePost(postId: String): Post?
    suspend fun unlikePost(postId: String): Post?
    suspend fun muteAccount(accountId: String): Relationship?
    suspend fun unmuteAccount(accountId: String): Relationship?

    suspend fun blockAccount(accountId: String): Relationship?
    suspend fun unblockAccount(accountId: String): Relationship?

    fun getAccountsFollowers(accountId: String): Flow<Resource<List<Account>>>
    fun getAccountsFollowing(accountId: String): Flow<Resource<List<Account>>>

    fun getMutedAccounts(): Flow<Resource<List<Account>>>
    fun getBlockedAccounts(): Flow<Resource<List<Account>>>

    fun getNotifications(): Flow<Resource<List<Notification>>>

    suspend fun getPostsByAccountId(accountId: String): List<Post>
    suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post>

    suspend fun getPostById(postId: String): Post?

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}