package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.Path

interface CountryRepository {
    suspend fun getTrendingPosts(range: String): List<Post>

    suspend fun getLocalTimeline(): List<Post>

    suspend fun getReplies(userid: String, postid: String): List<Reply>

    suspend fun getAccount(accountId: String): Account

    suspend fun getPostsByAccountId(accountId: String): List<Post>

    suspend fun getPostById(postId: String): Post?
}