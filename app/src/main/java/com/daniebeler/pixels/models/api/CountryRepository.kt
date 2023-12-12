package com.daniebeler.pixels.models.api

interface CountryRepository {
    suspend fun getTrendingPosts(range: String): List<Post>

    suspend fun getLocalTimeline(): List<Post>

    suspend fun getReplies(userid: String, postid: String): List<Reply>
}