package com.daniebeler.pfpixelix.data.remote

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.ApiReplyElementDto
import com.daniebeler.pfpixelix.data.remote.dto.ApplicationDto
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateReplyDto
import com.daniebeler.pfpixelix.data.remote.dto.InstanceDto
import com.daniebeler.pfpixelix.data.remote.dto.MediaAttachmentDto
import com.daniebeler.pfpixelix.data.remote.dto.NotificationDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.SearchDto
import com.daniebeler.pfpixelix.data.remote.dto.TagDto
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PixelfedApi {


    // Discover


    @GET("api/pixelfed/v2/discover/posts/trending")
    fun getTrendingPosts(@Query("range") range: String): Call<List<PostDto>>

    @GET("api/v1.1/discover/posts/hashtags?range=daily&_pe=1")
    fun getTrendingHashtags(): Call<List<TagDto>>

    @GET("/api/v1.1/discover/accounts/popular?range=daily")
    fun getTrendingAccounts(): Call<List<AccountDto>>


    // Timelines


    @GET("api/v1/timelines/tag/{tag}?_pe=1&limit=" + Constants.HASHTAG_TIMELINE_POSTS_LIMIT)
    fun getHashtagTimeline(
        @Path("tag") tag: String): Call<List<PostDto>>

    @GET("api/v1/timelines/tag/{tag}?_pe=1&limit=" + Constants.HASHTAG_TIMELINE_POSTS_LIMIT)
    fun getHashtagTimeline(
        @Path("tag") tag: String,
        @Query("max_id") maxPostId: String
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/public?local&_pe=1&limit=" + Constants.LOCAL_TIMELINE_POSTS_LIMIT)
    fun getLocalTimeline(): Call<List<PostDto>>

    @GET("api/v1/timelines/public?local&_pe=1&limit=" + Constants.LOCAL_TIMELINE_POSTS_LIMIT)
    fun getLocalTimeline(
        @Query("max_id") maxPostId: String): Call<List<PostDto>>

    @GET("api/v1/timelines/public?remote&_pe=1&limit=" + Constants.GLOBAL_TIMELINE_POSTS_LIMIT)
    fun getGlobalTimeline(): Call<List<PostDto>>

    @GET("/api/v1/timelines/public?remote&_pe=1&limit=" + Constants.GLOBAL_TIMELINE_POSTS_LIMIT)
    fun getGlobalTimeline(
        @Query("max_id") maxPostId: String): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1&limit=" + Constants.HOME_TIMELINE_POSTS_LIMIT)
    fun getHomeTimeline(): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1&limit=" + Constants.HOME_TIMELINE_POSTS_LIMIT)
    fun getHomeTimeline(
        @Query("max_id") maxPostId: String
    ): Call<List<PostDto>>


    // Favourites


    @GET("api/v1/favourites/?limit=" + Constants.LIKED_POSTS_LIMIT)
    fun getLikedPosts(): Call<List<PostDto>>

    @GET("api/v1/favourites/?limit=" + Constants.LIKED_POSTS_LIMIT)
    fun getLikedPosts(
        @Query("max_id") maxId: String
    ): Call<List<PostDto>>

    @GET("api/v1/statuses/{postId}/favourited_by?_pe=1&limit=" + Constants.LIKED_BY_LIMIT)
    fun getAccountsWhoLikedPost(
        @Path("postId") postId: String
    ): Call<List<AccountDto>>


    // Notifications


    @GET("api/v1/notifications?limit=" + Constants.NOTIFICATIONS_LIMIT)
    fun getNotifications(): Call<List<NotificationDto>>

    @GET("api/v1/notifications?limit=" + Constants.NOTIFICATIONS_LIMIT)
    fun getNotifications(
        @Query("max_id") maxNotificationId: String
    ): Call<List<NotificationDto>>


    // Accounts


    @GET("api/pixelfed/v1/accounts/{accountid}")
    fun getAccount(
        @Path("accountid") accountId: String
    ): Call<AccountDto>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=" + Constants.PROFILE_POSTS_LIMIT)
    fun getPostsByAccountId(
        @Path("accountid") accountId: String
    ): Call<List<PostDto>>

    @GET("api/pixelfed/v1/accounts/{accountid}/statuses?limit=" + Constants.PROFILE_POSTS_LIMIT)
    fun getPostsByAccountId(
        @Path("accountid") accountId: String, @Query("max_id") maxId: String
    ): Call<List<PostDto>>

    @GET("api/v1/accounts/relationships")
    fun getRelationships(
        @Query("id[]") userId: List<String>
    ): Call<List<RelationshipDto>>

    @GET("api/v1.1/accounts/mutuals/{id}")
    fun getMutalFollowers(
        @Path("id") userId: String
    ): Call<List<AccountDto>>

    @POST("api/v1/accounts/{id}/follow")
    fun followAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unfollow")
    fun unfollowAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/mute")
    fun muteAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unmute")
    fun unmuteAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/block")
    fun blockAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @POST("api/v1/accounts/{id}/unblock")
    fun unblockAccount(
        @Path("id") userId: String
    ): Call<RelationshipDto>

    @GET("api/v1/accounts/{id}/followers?limit=40")
    fun getAccountsFollowers(
        @Path("id") userId: String, @Query("max_id") maxId: String
    ): Call<List<AccountDto>>

    @GET("api/v1/accounts/{id}/following?limit=40")
    fun getAccountsFollowing(
        @Path("id") userId: String): Call<List<AccountDto>>

    @GET("api/v1/accounts/{id}/following?limit=40")
    fun getAccountsFollowing(
        @Path("id") userId: String,
        @Query("max_id") maxId: String
    ): Call<List<AccountDto>>

    @GET("api/v1/accounts/{id}/followers?limit=40")
    fun getAccountsFollowers(
        @Path("id") userId: String
    ): Call<List<AccountDto>>

    @GET("api/v1/accounts/verify_credentials")
    fun verifyToken(): Call<AccountDto>


    // Statuses


    @GET("api/v1/statuses/{postid}?_pe=1")
    fun getPostById(
        @Path("postid") postId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/favourite")
    fun likePost(@Path("id") userId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/unfavourite")
    fun unlikePost(
        @Path("id") userId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/bookmark")
    fun bookmarkPost(
        @Path("id") userId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/unbookmark")
    fun unbookmarkPost(
        @Path("id") userId: String): Call<PostDto>


    // Tags


    @POST("api/v1/tags/{id}/follow")
    fun followHashtag(
        @Path("id") tagId: String): Call<TagDto>

    @POST("api/v1/tags/{id}/unfollow")
    fun unfollowHashtag(
        @Path("id") tagId: String): Call<TagDto>

    @GET("api/v1/followed_tags?_pe=1")
    fun getFollowedHashtags(): Call<List<TagDto>>

    @GET("api/v1/tags/{tag}?_pe=1")
    fun getHashtag(@Path("tag") tag: String): Call<TagDto>


    // Other


    @GET("api/v1/bookmarks?limit=12")
    fun getBookmarkedPosts(): Call<List<PostDto>>

    @GET("api/v2/comments/{userid}/status/{postid}")
    fun getReplies(
        @Path("userid") userid: String, @Path("postid") postid: String
    ): Call<ApiReplyElementDto>

    @GET("api/v1/instance")
    fun getInstance(): Call<InstanceDto>

    @GET("api/v1/mutes")
    fun getMutedAccounts(): Call<List<AccountDto>>

    @GET("api/v1/blocks")
    fun getBlockedAccounts(): Call<List<AccountDto>>

    @GET("/api/v2/search?limit=5&_pe=1")
    fun getSearch(@Query("q") searchText: String
    ): Call<SearchDto>

    @POST("/api/v2/media")
    fun uploadMedia(@Body body: RequestBody
    ): Call<MediaAttachmentDto>

    @FormUrlEncoded
    @PUT("/api/v1/media/{id}")
    fun updateMedia(
        @Path("id") mediaAttachmentid: String,
        @Field("description") description: String,
    ): Call<MediaAttachmentDto>

    @POST("/api/v1/statuses")
    suspend fun createPost(@Body createPostDto: CreatePostDto
    ): Response<PostDto>

    @POST("/api/v1/statuses")
    fun createReply(@Body createReplyDto: CreateReplyDto
    ): Call<PostDto>

    @DELETE("/api/v1/statuses/{id}")
    fun deletePost(@Path("id") postid: String
    ): Call<PostDto>

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
}