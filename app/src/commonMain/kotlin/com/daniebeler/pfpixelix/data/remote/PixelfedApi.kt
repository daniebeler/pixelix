package com.daniebeler.pfpixelix.data.remote

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.data.remote.dto.AccessTokenDto
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.ApplicationDto
import com.daniebeler.pfpixelix.data.remote.dto.ChatDto
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.ConversationDto
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
    suspend fun getTrendingPosts(@Query("range") range: String): List<PostDto>

    @GET("api/v1.1/discover/posts/hashtags?_pe=1")
    suspend fun getTrendingHashtags(): List<TagDto>

    @GET("api/v1.1/discover/accounts/popular")
    suspend fun getTrendingAccounts(): List<AccountDto>


    // Timelines
    @GET("api/v1/timelines/tag/{tag}?_pe=1")
    suspend fun getHashtagTimeline(
        @Path("tag") tag: String,
        @Query("max_id") maxPostId: String? = null,
        @Query("limit") limit: Int
    ): List<PostDto>

    @GET("api/v1/timelines/public?local=true&_pe=1")
    suspend fun getLocalTimeline(
        @Query("max_id") maxPostId: String? = null,
        @Query("limit") limit: Int = Constants.LOCAL_TIMELINE_POSTS_LIMIT
    ): List<PostDto>

    @GET("api/v1/timelines/public?remote=true&_pe=1")
    suspend fun getGlobalTimeline(
        @Query("max_id") maxPostId: String? = null,
        @Query("limit") limit: Int = Constants.GLOBAL_TIMELINE_POSTS_LIMIT
    ): List<PostDto>

    @GET("api/v1/timelines/home?_pe=1")
    suspend fun getHomeTimeline(
        @Query("max_id") maxPostId: String? = null,
        @Query("include_reblogs") includeReblogs: Boolean = false,
        @Query("limit") limit: Int = Constants.HOME_TIMELINE_POSTS_LIMIT
    ): List<PostDto>

    // Favourites
    @GET("api/v1/favourites/?limit=" + Constants.LIKED_POSTS_LIMIT)
    fun getLikedPosts(
        @Query("max_id") maxId: String? = null,
        @Query("limit") limit: Int = Constants.LIKED_POSTS_LIMIT
    ): Call<List<PostDto>>

    @GET("api/v1/statuses/{postId}/favourited_by?_pe=1")
    suspend fun getAccountsWhoLikedPost(
        @Path("postId") postId: String,
        @Query("limit") limit: Int = Constants.LIKED_BY_LIMIT
    ): List<AccountDto>


    // Notifications
    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query("max_id") maxNotificationId: String? = null,
        @Query("limit") limit: Int = Constants.NOTIFICATIONS_LIMIT
    ): List<NotificationDto>


    // Accounts
    @GET("api/pixelfed/v1/accounts/{accountid}")
    suspend fun getAccount(
        @Path("accountid") accountId: String
    ): AccountDto

    @GET("api/v1.1/accounts/username/{username}?_pe=1")
    suspend fun getAccountByUsername(
        @Path("username") username: String
    ): AccountDto

    @POST("api/v1/accounts/update_credentials?_pe=1")
    suspend fun updateAccount(
        @Body body: MultiPartFormDataContent
    ): AccountDto

    @GET("api/v1/accounts/{accountid}/statuses?pe=1")
    suspend fun getPostsByAccountId(
        @Path("accountid") accountId: String,
        @Query("max_id") maxId: String? = null,
        @Query("limit") limit: Int
    ): List<PostDto>

    @GET("api/v1/accounts/relationships")
    suspend fun getRelationships(
        @Query("id[]") userId: List<String>
    ): List<RelationshipDto>

    @GET("api/v1.1/accounts/mutuals/{id}")
    suspend fun getMutalFollowers(
        @Path("id") userId: String
    ): List<AccountDto>

    @POST("api/v1/accounts/{id}/follow")
    suspend fun followAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @POST("api/v1/accounts/{id}/unfollow")
    suspend fun unfollowAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @POST("api/v1/accounts/{id}/mute")
    suspend fun muteAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @POST("api/v1/accounts/{id}/unmute")
    suspend fun unmuteAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @POST("api/v1/accounts/{id}/block")
    suspend fun blockAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @POST("api/v1/accounts/{id}/unblock")
    suspend fun unblockAccount(
        @Path("id") userId: String
    ): RelationshipDto

    @GET("api/v1/accounts/{id}/followers")
    suspend fun getAccountsFollowers(
        @Path("id") userId: String,
        @Query("max_id") maxId: String? = null,
        @Query("limit") limit: Int = Constants.FOLLOWERS_LIMIT
    ): List<AccountDto>

    @GET("api/v1/accounts/{id}/following")
    suspend fun getAccountsFollowing(
        @Path("id") userId: String,
        @Query("max_id") maxId: String? = null,
        @Query("limit") limit: Int = Constants.FOLLOWERS_LIMIT
    ): List<AccountDto>

    // Statuses
    @GET("api/v1/statuses/{postid}?_pe=1")
    suspend fun getPostById(
        @Path("postid") postId: String
    ): PostDto

    @POST("api/v1/statuses/{id}/favourite")
    suspend fun likePost(@Path("id") userId: String): PostDto

    @POST("api/v1/statuses/{id}/unfavourite")
    suspend fun unlikePost(
        @Path("id") userId: String
    ): PostDto

    @POST("api/v1/statuses/{id}/reblog")
    suspend fun reblogPost(@Path("id") userId: String): PostDto

    @POST("api/v1/statuses/{id}/unreblog")
    suspend fun unreblogPost(
        @Path("id") userId: String
    ): PostDto

    @POST("api/v1/statuses/{id}/bookmark")
    suspend fun bookmarkPost(
        @Path("id") userId: String
    ): PostDto

    @POST("api/v1/statuses/{id}/unbookmark")
    suspend fun unbookmarkPost(
        @Path("id") userId: String
    ): PostDto


    // Collections
    @GET("api/v1.1/collections/accounts/{userId}")
    suspend fun getCollectionsByUserId(
        @Path("userId") userId: String, @Query("page") page: Int
    ): List<CollectionDto>

    @GET("api/v1.1/collections/view/{collectionid}")
    suspend fun getCollection(
        @Path("collectionid") collectionId: String
    ): CollectionDto

    @GET("api/v1.1/collections/items/{collectionid}")
    suspend fun getPostsOfCollection(
        @Path("collectionid") collectionId: String
    ): List<PostDto>

    @POST("api/v1.1/collections/remove")
    suspend fun removePostOfCollection(
        @Query("collection_id") collectionId: String, @Query("post_id") postId: String
    ): String

    @POST("api/v1.1/collections/add")
    suspend fun addPostOfCollection(
        @Query("collection_id") collectionId: String, @Query("post_id") postId: String
    ): String

    @FormUrlEncoded
    @POST("api/v1.1/collections/update/{collectionId}")
    suspend fun updateCollection(
        @Path("collectionId") collectionId: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("visibility") visibility: String
    ): CollectionDto

    // Tags
    @POST("api/v1/tags/{id}/follow")
    suspend fun followHashtag(
        @Path("id") tagId: String
    ): TagDto

    @POST("api/v1/tags/{id}/unfollow")
    suspend fun unfollowHashtag(
        @Path("id") tagId: String
    ): TagDto

    @GET("api/v1/followed_tags?_pe=1")
    suspend fun getFollowedHashtags(): List<TagDto>

    @GET("api/v1/tags/{tag}?_pe=1")
    suspend fun getHashtag(@Path("tag") tag: String): TagDto

    @GET("api/v1/tags/{tag}/related")
    suspend fun getRelatedHashtags(@Path("tag") tag: String): List<RelatedHashtagDto>


    // Direct Messages
    @GET("api/v1/conversations")
    suspend fun getConversations(): List<ConversationDto>

    @GET("api/v1.1/direct/thread")
    suspend fun getChat(
        @Query("pid") accountId: String,
        @Query("max_id") maxId: String? = null
    ): ChatDto

    @Headers("Content-Type: application/json")
    @POST("api/v1.1/direct/thread/send")
    suspend fun sendMessage(@Body createMessageDto: String): MessageDto

    @DELETE("api/v1.1/direct/thread/message")
    suspend fun deleteMessage(@Query("id") id: String): List<Int>
    // Other

    @GET("api/v1/bookmarks")
    suspend fun getBookmarkedPosts(
        @Query("limit") limit: Int = Constants.BOOKMARKED_LIMIT
    ): List<PostDto>

    @GET("api/v1/statuses/{postid}/context?_pe=1")
    suspend fun getReplies(
        @Path("postid") postid: String
    ): PostContextDto

    @GET("api/v1/instance")
    suspend fun getInstance(): InstanceDto

    @GET("api/v1/mutes")
    suspend fun getMutedAccounts(): List<AccountDto>

    @GET("api/v1/blocks")
    suspend fun getBlockedAccounts(): List<AccountDto>

    @GET("api/v2/search?_pe=1&resolve")
    suspend fun getSearch(
        @Query("q") searchText: String, @Query("type") type: String?, @Query("limit") limit: Int
    ): SearchDto

    @GET("api/v1.1/compose/search/location?limit=5")
    suspend fun searchLocations(
        @Query("q") searchText: String
    ): List<PlaceDto>

    @POST("api/v2/media")
    suspend fun uploadMedia(
        @Body body: MultiPartFormDataContent
    ): MediaAttachmentDto

    @FormUrlEncoded
    @PUT("api/v1/media/{id}")
    suspend fun updateMedia(
        @Path("id") mediaAttachmentid: String,
        @Field("description") description: String,
    ): MediaAttachmentDto

    @Headers("Content-Type: application/json")
    @POST("api/v1/statuses")
    suspend fun createPost(
        @Body createPostDto: String
    ): PostDto

    @Headers("Content-Type: application/json")
    @POST("api/v1/statuses")
    suspend fun createReply(
        @Body createReplyDto: String
    ): PostDto

    @Headers("Content-Type: application/json")
    @PUT("api/v1/statuses/{id}")
    suspend fun updatePost(
        @Path("id") postId: String, @Body updatePostDto: String
    )

    @DELETE("api/v1/statuses/{id}")
    suspend fun deletePost(
        @Path("id") postid: String
    ): PostDto

    @GET("api/pixelfed/v1/web/settings")
    suspend fun getSettings(): SettingsDto

    @GET
    suspend fun getNodeInfo(@Url domain: String): NodeInfoDto

    @GET("https://api.fedidb.org/v1/software/{slug}")
    suspend fun getSoftwareFromFediDB(
        @Path("slug") slug: String
    ): FediSoftwareDto

    @GET("https://api.fedidb.org/v1/server/domain/{slug}")
    suspend fun getServerFromFediDB(
        @Path("slug") domain: String
    ): WrapperDto
}