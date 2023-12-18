package com.daniebeler.pixels.models.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PixelfedApi {
    @GET("api/pixelfed/v2/discover/posts/trending")
    fun getTrendingPosts(@Query("range") range: String): Call<List<PostDTO>>

    @GET("api/pixelfed/v1/timelines/public")
    fun getLocalTimeline(): Call<List<PostDTO>>

    @GET("api/v2/comments/{userid}/status/{postid}")
    fun getReplies(@Path("userid") userid: String, @Path("postid") postid: String): Call<ApiReplyElement>

    @GET("api/pixelfed/v1/accounts/{accountid}")
    fun getAccount(@Path("accountid") accountId: String): Call<Account>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=10")
    fun getPostsByAccountId(@Path("accountid") accountId: String): Call<List<PostDTO>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=10")
    fun getPostsByAccountId(@Path("accountid") accountId: String, @Query("max_id") maxId: String): Call<List<PostDTO>>

    @GET("api/v1/statuses/{postid}")
    fun getPostById(@Path("postid") postId: String): Call<Post>


    @POST("api/v1/apps?client_name=pixels&redirect_uris=pixels-android-auth://callback")
    fun createApplication(): Call<Application>

    @FormUrlEncoded
    @POST("oauth/token")
    fun obtainToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String? = "pixels-android-auth://callback",
        @Field("grant_type") grantType: String? = "authorization_code"
    ): Call<AccessToken>
}