package com.daniebeler.pixels.api

import com.daniebeler.pixels.domain.model.AccessToken
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Relationship
import com.daniebeler.pixels.domain.model.Reply
import com.daniebeler.pixels.domain.model.Tag

interface CountryRepository {
    fun setBaseUrl(baseUrl: String)
    fun setAccessToken(token: String)
    suspend fun getTrendingPosts(range: String): List<Post>
    suspend fun getTrendingHashtags(): List<Tag>

    suspend fun getHashtagTimeline(hashtag: String): List<Post>

    suspend fun getLocalTimeline(): List<Post>

    suspend fun getHomeTimeline(): List<Post>

    suspend fun getRelationships(userId: String): List<Relationship>

    suspend fun getMutalFollowers(userId: String): List<Account>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    suspend fun getAccount(accountId: String): Account?

    suspend fun followAccount(accountId: String): Relationship?

    suspend fun unfollowAccount(accountId: String): Relationship?

    suspend fun getNotifications(): List<Notification>

    suspend fun getPostsByAccountId(accountId: String): List<Post>
    suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post>

    suspend fun getPostById(postId: String): Post?

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}