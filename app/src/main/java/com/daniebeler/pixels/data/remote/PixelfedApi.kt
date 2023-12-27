package com.daniebeler.pixels.data.remote

import com.daniebeler.pixels.data.remote.dto.AccessTokenDto
import com.daniebeler.pixels.data.remote.dto.AccountDto
import com.daniebeler.pixels.data.remote.dto.ApiReplyElementDto
import com.daniebeler.pixels.data.remote.dto.ApplicationDto
import com.daniebeler.pixels.data.remote.dto.NotificationDto
import com.daniebeler.pixels.data.remote.dto.PostDto
import com.daniebeler.pixels.data.remote.dto.RelationshipDto
import com.daniebeler.pixels.data.remote.dto.TagDto
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
    fun getTrendingPosts(@Query("range") range: String): Call<List<PostDto>>

    @GET("api/v1.1/discover/posts/hashtags?range=daily")
    fun getTrendingHashtags(@Header("Authorization") token: String): Call<List<TagDto>>

    @GET("api/v1/timelines/tag/{tag}")
    fun getHashtagTimeline(@Path("tag") tag: String, @Header("Authorization") token: String): Call<List<PostDto>>

    @GET("api/v1/timelines/public")
    fun getLocalTimeline(@Header("Authorization") token: String): Call<List<PostDto>>

    @GET("api/v1/timelines/home")
    fun getHomeTimeline(@Header("Authorization") accessToken: String): Call<List<PostDto>>

    @GET("api/v1/timelines/home")
    fun getHomeTimeline(@Query("max_id") maxPostId: String, @Header("Authorization") accessToken: String): Call<List<PostDto>>

    @GET("api/v2/comments/{userid}/status/{postid}")
    fun getReplies(@Path("userid") userid: String, @Path("postid") postid: String): Call<ApiReplyElementDto>

    @GET("api/pixelfed/v1/accounts/{accountid}")
    fun getAccount(@Path("accountid") accountId: String, @Header("Authorization") token: String): Call<AccountDto>

    @GET("api/v1/notifications")
    fun getNotifications(@Header("Authorization") token: String): Call<List<NotificationDto>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(@Path("accountid") accountId: String, @Header("Authorization") token: String): Call<List<PostDto>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(@Path("accountid") accountId: String, @Header("Authorization") token: String, @Query("max_id") maxId: String): Call<List<PostDto>>

    @GET("api/v1/statuses/{postid}")
    fun getPostById(@Path("postid") postId: String, @Header("Authorization") token: String): Call<PostDto>

    @GET("api/v1/accounts/relationships")
    fun getRelationships(@Query("id[]") userId: String, @Header("Authorization") token: String): Call<List<RelationshipDto>>

    @GET("api/v1.1/accounts/mutals/{id}")
    fun getMutalFollowers(@Query("id") userId: String, @Header("Authorization") token: String): Call<List<AccountDto>>

    @POST("api/v1/accounts/{id}/follow")
    fun followAccount(@Path("id") userId: String, @Header("Authorization") token: String): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unfollow")
    fun unfollowAccount(@Path("id") userId: String, @Header("Authorization") token: String): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/mute")
    fun muteAccount(@Path("id") userId: String, @Header("Authorization") token: String): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unmute")
    fun unmuteAccount(@Path("id") userId: String, @Header("Authorization") token: String): Call<RelationshipDto>

    @POST("api/v1/apps?client_name=pixels&redirect_uris=pixels-android-auth://callback")
    fun createApplication(): Call<ApplicationDto>

    @FormUrlEncoded
    @POST("oauth/token")
    fun obtainToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String? = "pixels-android-auth://callback",
        @Field("grant_type") grantType: String? = "authorization_code"
    ): Call<AccessTokenDto>

    @GET("api/v1/accounts/verify_credentials")
    fun verifyToken(@Header("Authorization") token: String): Call<AccountDto>
}