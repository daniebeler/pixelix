package com.daniebeler.pixels.api

import com.daniebeler.pixels.api.models.AccessToken
import com.daniebeler.pixels.api.models.Account
import com.daniebeler.pixels.api.models.ApiReplyElement
import com.daniebeler.pixels.api.models.Application
import com.daniebeler.pixels.api.models.Hashtag
import com.daniebeler.pixels.api.models.Notification
import com.daniebeler.pixels.api.models.Post
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PixelfedApi {
    @GET("api/pixelfed/v2/discover/posts/trending")
    fun getTrendingPosts(@Query("range") range: String): Call<List<Post>>

    @GET("api/v1.1/discover/posts/hashtags")
    fun getTrendingHashtags(@Header("Authorization") token: String): Call<List<Hashtag>>

    @GET("api/v1/timelines/tag/{tag}")
    fun getHashtagTimeline(@Path("tag") tag: String, @Header("Authorization") token: String): Call<List<Post>>

    @GET("api/v1/timelines/public")
    fun getLocalTimeline(@Header("Authorization") token: String): Call<List<Post>>

    @GET("api/v1/timelines/home")
    fun getHomeTimeline(@Header("Authorization") accessToken: String): Call<List<Post>>

    @GET("api/v2/comments/{userid}/status/{postid}")
    fun getReplies(@Path("userid") userid: String, @Path("postid") postid: String): Call<ApiReplyElement>

    @GET("api/pixelfed/v1/accounts/{accountid}")
    fun getAccount(@Path("accountid") accountId: String, @Header("Authorization") token: String): Call<Account>

    @GET("api/v1/notifications")
    fun getNotifications(@Header("Authorization") token: String): Call<List<Notification>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(@Path("accountid") accountId: String, @Header("Authorization") token: String): Call<List<Post>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(@Path("accountid") accountId: String, @Header("Authorization") token: String, @Query("max_id") maxId: String): Call<List<Post>>

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

    @GET("api/v1/accounts/verify_credentials")
    fun verifyToken(@Header("Authorization") token: String): Call<Account>
}