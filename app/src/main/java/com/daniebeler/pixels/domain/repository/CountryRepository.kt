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
import com.daniebeler.pixels.domain.model.Search
import com.daniebeler.pixels.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun doesAccessTokenExist(): Boolean

    suspend fun storeClientId(clientId: String)
    suspend fun storeBaseUrl(baseUrl: String)
    fun getClientIdFromStorage(): Flow<String>
    fun getBaseUrlFromStorage(): Flow<String>
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

    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun getTrendingAccounts(): Flow<Resource<List<Account>>>
    fun getHashtagTimeline(hashtag: String): Flow<Resource<List<Post>>>
    fun getLocalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getGlobalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getHomeTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getLikedPosts(): Flow<Resource<List<Post>>>
    fun getBookmarkedPosts(): Flow<Resource<List<Post>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>

    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>

    fun getMutalFollowers(userId: String): Flow<Resource<List<Account>>>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    fun getAccount(accountId: String): Flow<Resource<Account?>>

    fun followAccount(accountId: String): Flow<Resource<Relationship>>

    fun unfollowAccount(accountId: String): Flow<Resource<Relationship>>

    fun followHashtag(tagId: String): Flow<Resource<Tag>>

    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>

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

    fun getPostsByAccountId(accountId: String, maxPostId: String = ""): Flow<Resource<List<Post>>>

    fun getPostById(postId: String): Flow<Resource<Post?>>

    fun search(searchText: String): Flow<Resource<Search>>

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}