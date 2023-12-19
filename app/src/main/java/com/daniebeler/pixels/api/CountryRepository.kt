package com.daniebeler.pixels.api

import com.daniebeler.pixels.api.models.AccessToken
import com.daniebeler.pixels.api.models.Account
import com.daniebeler.pixels.api.models.Application
import com.daniebeler.pixels.api.models.Hashtag
import com.daniebeler.pixels.api.models.Notification
import com.daniebeler.pixels.api.models.Post
import com.daniebeler.pixels.api.models.Reply

interface CountryRepository {
    fun setBaseUrl(baseUrl: String)
    fun setAccessToken(token: String)
    suspend fun getTrendingPosts(range: String): List<Post>
    suspend fun getTrendingHashtags(): List<Hashtag>

    suspend fun getLocalTimeline(): List<Post>

    suspend fun getHomeTimeline(accessToken: String): List<Post>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    suspend fun getAccount(accountId: String): Account
    suspend fun getNotifications(): List<Notification>

    suspend fun getPostsByAccountId(accountId: String): List<Post>
    suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post>

    suspend fun getPostById(postId: String): Post?

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}