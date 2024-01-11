package com.daniebeler.pixelix.data.remote

import com.daniebeler.pixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pixelix.data.remote.dto.AccountDto
import com.daniebeler.pixelix.data.remote.dto.ApiReplyElementDto
import com.daniebeler.pixelix.data.remote.dto.ApplicationDto
import com.daniebeler.pixelix.data.remote.dto.NotificationDto
import com.daniebeler.pixelix.data.remote.dto.PostDto
import com.daniebeler.pixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pixelix.data.remote.dto.SearchDto
import com.daniebeler.pixelix.data.remote.dto.TagDto
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

    @GET("api/v1.1/discover/posts/hashtags?range=daily&_pe=1")
    fun getTrendingHashtags(@Header("Authorization") token: String): Call<List<TagDto>>

    @GET("/api/v1.1/discover/accounts/popular?range=daily")
    fun getTrendingAccounts(@Header("Authorization") token: String): Call<List<AccountDto>>

    @GET("api/v1/timelines/tag/{tag}?_pe=1")
    fun getHashtagTimeline(
        @Path("tag") tag: String,
        @Header("Authorization") token: String
    ): Call<List<PostDto>>

    @GET("api/v1/tags/{tag}?_pe=1")
    fun getHashtag(@Path("tag") tag: String, @Header("Authorization") token: String): Call<TagDto>

    @GET("api/v1/timelines/public?local&_pe=1")
    fun getLocalTimeline(@Header("Authorization") token: String): Call<List<PostDto>>

    @GET("api/v1/timelines/public?local&_pe=1")
    fun getLocalTimeline(
        @Query("max_id") maxPostId: String,
        @Header("Authorization") token: String
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/public?remote&_pe=1")
    fun getGlobalTimeline(@Header("Authorization") token: String): Call<List<PostDto>>

    @GET("/api/v1/timelines/public?remote&_pe=1")
    fun getGlobalTimeline(
        @Query("max_id") maxPostId: String,
        @Header("Authorization") token: String
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1")
    fun getHomeTimeline(@Header("Authorization") accessToken: String): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1")
    fun getHomeTimeline(
        @Query("max_id") maxPostId: String,
        @Header("Authorization") accessToken: String
    ): Call<List<PostDto>>

    @GET("api/v1/favourites?limit=12")
    fun getLikedPosts(@Header("Authorization") accessToken: String): Call<List<PostDto>>

    @GET("api/v1/bookmarks?limit=12")
    fun getBookmarkedPosts(@Header("Authorization") accessToken: String): Call<List<PostDto>>

    @GET("api/v1/followed_tags?_pe=1")
    fun getFollowedHashtags(@Header("Authorization") accessToken: String): Call<List<TagDto>>

    @GET("api/v2/comments/{userid}/status/{postid}")
    fun getReplies(
        @Path("userid") userid: String,
        @Path("postid") postid: String
    ): Call<ApiReplyElementDto>

    @GET("api/pixelfed/v1/accounts/{accountid}")
    fun getAccount(
        @Path("accountid") accountId: String,
        @Header("Authorization") token: String
    ): Call<AccountDto>

    @GET("api/v1/notifications")
    fun getNotifications(
        @Header("Authorization") token: String
    ): Call<List<NotificationDto>>

    @GET("api/v1/notifications")
    fun getNotifications(
        @Header("Authorization") token: String,
        @Query("max_id") maxNotificationId: String
    ): Call<List<NotificationDto>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(
        @Path("accountid") accountId: String,
        @Header("Authorization") token: String
    ): Call<List<PostDto>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=12")
    fun getPostsByAccountId(
        @Path("accountid") accountId: String,
        @Header("Authorization") token: String,
        @Query("max_id") maxId: String
    ): Call<List<PostDto>>

    @GET("api/v1/statuses/{postid}?_pe=1")
    fun getPostById(
        @Path("postid") postId: String,
        @Header("Authorization") token: String
    ): Call<PostDto>

    @GET("api/v1/accounts/relationships")
    fun getRelationships(
        @Query("id[]") userId: List<String>,
        @Header("Authorization") token: String
    ): Call<List<RelationshipDto>>

    @GET("api/v1.1/accounts/mutuals/{id}")
    fun getMutalFollowers(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<List<AccountDto>>

    @POST("api/v1/accounts/{id}/follow")
    fun followAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unfollow")
    fun unfollowAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/tags/{id}/follow")
    fun followHashtag(
        @Path("id") tagId: String,
        @Header("Authorization") token: String
    ): Call<TagDto>

    @POST("api/v1/tags/{id}/unfollow")
    fun unfollowHashtag(
        @Path("id") tagId: String,
        @Header("Authorization") token: String
    ): Call<TagDto>

    @POST("api/v1/accounts/{id}/mute")
    fun muteAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unmute")
    fun unmuteAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/block")
    fun blockAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unblock")
    fun unblockAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<RelationshipDto>

    @POST("api/v1/statuses/{id}/favourite")
    fun likePost(@Path("id") userId: String, @Header("Authorization") token: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/unfavourite")
    fun unlikePost(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/bookmark")
    fun bookmarkPost(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/unbookmark")
    fun unbookmarkPost(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<PostDto>

    @GET("api/v1/accounts/{id}/followers?limit=40")
    fun getAccountsFollowers(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<List<AccountDto>>

    @GET("api/v1/accounts/{id}/following?limit=40")
    fun getAccountsFollowing(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<List<AccountDto>>

    @GET("api/v1/mutes")
    fun getMutedAccounts(@Header("Authorization") accessToken: String): Call<List<AccountDto>>

    @GET("api/v1/blocks")
    fun getBlockedAccounts(@Header("Authorization") accessToken: String): Call<List<AccountDto>>

    @GET("/api/v2/search?limit=5&_pe=1")
    fun getSearch(
        @Header("Authorization") accessToken: String,
        @Query("q") searchText: String
    ): Call<SearchDto>

    @POST("api/v1/apps?client_name=pixelix&redirect_uris=pixelix-android-auth://callback")
    fun createApplication(): Call<ApplicationDto>

    @FormUrlEncoded
    @POST("oauth/token")
    fun obtainToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String? = "pixelix-android-auth://callback",
        @Field("grant_type") grantType: String? = "authorization_code"
    ): Call<AccessTokenDto>

    @GET("api/v1/accounts/verify_credentials")
    fun verifyToken(@Header("Authorization") token: String): Call<AccountDto>
}