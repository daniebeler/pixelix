package com.daniebeler.pfpixelix.data.remote

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.ApplicationDto
import com.daniebeler.pfpixelix.data.remote.dto.ChatDto
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.ConversationDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateMessageDto
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.CreateReplyDto
import com.daniebeler.pfpixelix.data.remote.dto.InstanceDto
import com.daniebeler.pfpixelix.data.remote.dto.MediaAttachmentDto
import com.daniebeler.pfpixelix.data.remote.dto.MessageDto
import com.daniebeler.pfpixelix.data.remote.dto.NotificationDto
import com.daniebeler.pfpixelix.data.remote.dto.PlaceDto
import com.daniebeler.pfpixelix.data.remote.dto.PostContextDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.RelatedHashtagDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.SearchDto
import com.daniebeler.pfpixelix.data.remote.dto.SettingsDto
import com.daniebeler.pfpixelix.data.remote.dto.TagDto
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.data.remote.dto.WellKnownDomainsDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.FediSoftwareDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.NodeInfoDto
import com.daniebeler.pfpixelix.data.remote.dto.nodeinfo.WrapperDto
import de.jensklingenberg.ktorfit.Call
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.Url
import io.ktor.client.request.forms.MultiPartFormDataContent

interface PixelfedApi {


    // Discover


    @GET("api/v1.1/discover/posts/trending")
    fun getTrendingPosts(@Query("range") range: String): Call<List<PostDto>>

    @GET("api/v1.1/discover/posts/hashtags?_pe=1")
    fun getTrendingHashtags(): Call<List<TagDto>>

    @GET("api/v1.1/discover/accounts/popular")
    fun getTrendingAccounts(): Call<List<AccountDto>>


    // Timelines


    @GET("api/v1/timelines/tag/{tag}?_pe=1")
    fun getHashtagTimeline(
        @Path("tag") tag: String, @Query("limit") limit: Int
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/tag/{tag}?_pe=1")
    fun getHashtagTimeline(
        @Path("tag") tag: String, @Query("max_id") maxPostId: String, @Query("limit") limit: Int
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/public?local=true&_pe=1&limit=" + Constants.LOCAL_TIMELINE_POSTS_LIMIT)
    fun getLocalTimeline(): Call<List<PostDto>>

    @GET("api/v1/timelines/public?local=true&_pe=1&limit=" + Constants.LOCAL_TIMELINE_POSTS_LIMIT)
    fun getLocalTimeline(
        @Query("max_id") maxPostId: String
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/public?remote=true&_pe=1&limit=" + Constants.GLOBAL_TIMELINE_POSTS_LIMIT)
    fun getGlobalTimeline(): Call<List<PostDto>>

    @GET("api/v1/timelines/public?remote=true&_pe=1&limit=" + Constants.GLOBAL_TIMELINE_POSTS_LIMIT)
    fun getGlobalTimeline(
        @Query("max_id") maxPostId: String
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1&limit=" + Constants.HOME_TIMELINE_POSTS_LIMIT)
    fun getHomeTimeline(@Query("include_reblogs") includeReblogs: Boolean): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1&limit=" + Constants.HOME_TIMELINE_POSTS_LIMIT)
    fun getHomeTimeline(
        @Query("max_id") maxPostId: String,
        @Query("include_reblogs") includeReblogs: Boolean
    ): Call<List<PostDto>>

    @GET("api/v1/timelines/home?_pe=1")
    fun getHomeTimelineWithLimit(
        @Query("limit") limit: Int
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

    @GET("api/v1.1/accounts/username/{username}?_pe=1")
    fun getAccountByUsername(
        @Path("username") username: String
    ): Call<AccountDto>

    @POST("api/v1/accounts/update_credentials?_pe=1")
    fun updateAccount(
        @Body body: MultiPartFormDataContent
    ): Call<AccountDto>

    @GET("api/v1/accounts/{accountid}/statuses?pe=1")
    fun getPostsByAccountId(
        @Path("accountid") accountId: String, @Query("limit") limit: Int
    ): Call<List<PostDto>>

    @GET("api/v1/accounts/{accountid}/statuses?pe=1")
    fun getPostsByAccountId(
        @Path("accountid") accountId: String,
        @Query("max_id") maxId: String,
        @Query("limit") limit: Int
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
        @Path("id") userId: String
    ): Call<List<AccountDto>>

    @GET("api/v1/accounts/{id}/following?limit=40")
    fun getAccountsFollowing(
        @Path("id") userId: String, @Query("max_id") maxId: String
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
        @Path("postid") postId: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/favourite")
    fun likePost(@Path("id") userId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/unfavourite")
    fun unlikePost(
        @Path("id") userId: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/reblog")
    fun reblogPost(@Path("id") userId: String): Call<PostDto>

    @POST("api/v1/statuses/{id}/unreblog")
    fun unreblogPost(
        @Path("id") userId: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/bookmark")
    fun bookmarkPost(
        @Path("id") userId: String
    ): Call<PostDto>

    @POST("api/v1/statuses/{id}/unbookmark")
    fun unbookmarkPost(
        @Path("id") userId: String
    ): Call<PostDto>


    // Collections

    @GET("api/v1.1/collections/accounts/{userId}")
    fun getCollectionsByUserId(
        @Path("userId") userId: String, @Query("page") page: Int
    ): Call<List<CollectionDto>>

    @GET("api/v1.1/collections/view/{collectionid}")
    fun getCollection(
        @Path("collectionid") collectionId: String
    ): Call<CollectionDto>

    @GET("api/v1.1/collections/items/{collectionid}")
    fun getPostsOfCollection(
        @Path("collectionid") collectionId: String
    ): Call<List<PostDto>>

    @POST("api/v1.1/collections/remove")
    fun removePostOfCollection(
        @Query("collection_id") collectionId: String, @Query("post_id") postId: String
    ): Call<String>

    @POST("api/v1.1/collections/add")
    fun addPostOfCollection(
        @Query("collection_id") collectionId: String, @Query("post_id") postId: String
    ): Call<String>

    @FormUrlEncoded
    @POST("api/v1.1/collections/update/{collectionId}")
    fun updateCollection(
        @Path("collectionId") collectionId: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("visibility") visibility: String
    ): Call<CollectionDto>

    // Tags


    @POST("api/v1/tags/{id}/follow")
    fun followHashtag(
        @Path("id") tagId: String
    ): Call<TagDto>

    @POST("api/v1/tags/{id}/unfollow")
    fun unfollowHashtag(
        @Path("id") tagId: String
    ): Call<TagDto>

    @GET("api/v1/followed_tags?_pe=1")
    fun getFollowedHashtags(): Call<List<TagDto>>

    @GET("api/v1/tags/{tag}?_pe=1")
    fun getHashtag(@Path("tag") tag: String): Call<TagDto>

    @GET("api/v1/tags/{tag}/related")
    fun getRelatedHashtags(@Path("tag") tag: String): Call<List<RelatedHashtagDto>>


    // Direct Messages

    @GET("api/v1/conversations")
    fun getConversations(): Call<List<ConversationDto>>

    @GET("api/v1.1/direct/thread")
    fun getChat(@Query("pid") accountId: String): Call<ChatDto>

    @GET("api/v1.1/direct/thread")
    fun getChat(@Query("pid") accountId: String, @Query("max_id") maxId: String): Call<ChatDto>

    @Headers("Content-Type: application/json")
    @POST("api/v1.1/direct/thread/send")
    fun sendMessage(@Body createMessageDto: String): Call<MessageDto>

    @DELETE("api/v1.1/direct/thread/message")
    fun deleteMessage(@Query("id") id: String): Call<List<Int>>
    // Other

    @GET("api/v1/bookmarks?limit=12")
    fun getBookmarkedPosts(): Call<List<PostDto>>

    @GET("api/v1/statuses/{postid}/context?_pe=1")
    fun getReplies(
        @Path("postid") postid: String
    ): Call<PostContextDto>

    @GET("api/v1/instance")
    fun getInstance(): Call<InstanceDto>

    @GET("api/v1/mutes")
    fun getMutedAccounts(): Call<List<AccountDto>>

    @GET("api/v1/blocks")
    fun getBlockedAccounts(): Call<List<AccountDto>>

    @GET("api/v2/search?_pe=1&resolve")
    fun getSearch(
        @Query("q") searchText: String, @Query("type") type: String?, @Query("limit") limit: Int
    ): Call<SearchDto>

    @GET("api/v1.1/compose/search/location?limit=5")
    fun searchLocations(
        @Query("q") searchText: String
    ): Call<List<PlaceDto>>

    @POST("api/v2/media")
    fun uploadMedia(
        @Body body: MultiPartFormDataContent
    ): Call<MediaAttachmentDto>

    @FormUrlEncoded
    @PUT("api/v1/media/{id}")
    fun updateMedia(
        @Path("id") mediaAttachmentid: String,
        @Field("description") description: String,
    ): Call<MediaAttachmentDto>

    @Headers("Content-Type: application/json")
    @POST("api/v1/statuses")
    suspend fun createPost(
        @Body createPostDto: String
    ): Call<PostDto>

    @Headers("Content-Type: application/json")
    @POST("api/v1/statuses")
    fun createReply(
        @Body createReplyDto: String
    ): Call<PostDto>

    @Headers("Content-Type: application/json")
    @PUT("api/v1/statuses/{id}")
    suspend fun updatePost(
        @Path("id") postId: String, @Body updatePostDto: String
    ): Call<Unit>

    @DELETE("api/v1/statuses/{id}")
    fun deletePost(
        @Path("id") postid: String
    ): Call<PostDto>

    @GET("api/pixelfed/v1/web/settings")
    fun getSettings(): Call<SettingsDto>

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

    @GET
    fun getWellKnownDomains(@Url domain: String): Call<WellKnownDomainsDto>

    @GET
    fun getNodeInfo(@Url domain: String): Call<NodeInfoDto>

    @GET("https://api.fedidb.org/v1/software/{slug}")
    fun getSoftwareFromFediDB(
        @Path("slug") slug: String
    ): Call<FediSoftwareDto>

    @GET("https://api.fedidb.org/v1/server/domain/{slug}")
    fun getServerFromFediDB(
        @Path("slug") domain: String
    ): Call<WrapperDto>
}