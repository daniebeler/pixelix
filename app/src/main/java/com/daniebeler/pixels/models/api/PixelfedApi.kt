package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PixelfedApi {
    @GET("pixelfed/v2/discover/posts/trending")
    fun getTrendingPosts(@Query("range") range: String): Call<List<PostDTO>>

    @GET("pixelfed/v1/timelines/public")
    fun getLocalTimeline(): Call<List<PostDTO>>

    @GET("v2/comments/{userid}/status/{postid}")
    fun getReplies(@Path("userid") userid: String, @Path("postid") postid: String): Call<ApiReplyElement>
}