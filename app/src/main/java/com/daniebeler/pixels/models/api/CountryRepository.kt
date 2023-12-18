package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.Path

interface CountryRepository {
    fun setBaseUrl(baseUrl: String)
    fun setAccessToken(token: String)
    suspend fun getTrendingPosts(range: String): List<Post>

    suspend fun getLocalTimeline(): List<Post>

    suspend fun getHomeTimeline(accessToken: String): List<Post>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    suspend fun getAccount(accountId: String): Account

    suspend fun getPostsByAccountId(accountId: String): List<Post>
    suspend fun getPostsByAccountId(accountId: String, maxPostId: String): List<Post>

    suspend fun getPostById(postId: String): Post?

    suspend fun createApplication(): Application?

    suspend fun obtainToken(clientId: String, clientSecret: String, code: String): AccessToken?

    suspend fun verifyToken(token: String): Account?
}