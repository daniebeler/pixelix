package com.daniebeler.pfpixelix.common

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.add_circle
import pixelix.app.generated.resources.add_circle_outline
import pixelix.app.generated.resources.alerts
import pixelix.app.generated.resources.bookmark_outline
import pixelix.app.generated.resources.home
import pixelix.app.generated.resources.house
import pixelix.app.generated.resources.house_fill
import pixelix.app.generated.resources.notifications
import pixelix.app.generated.resources.notifications_outline
import pixelix.app.generated.resources.profile
import pixelix.app.generated.resources.search
import pixelix.app.generated.resources.search_outline

sealed class Destinations(
    val route: String,
    val icon: DrawableResource = Res.drawable.bookmark_outline,
    val activeIcon: DrawableResource = Res.drawable.bookmark_outline,
    val label: StringResource = Res.string.home
) {
    data object HomeScreen : Destinations(
        route = "home_screen",
        icon = Res.drawable.house,
        activeIcon = Res.drawable.house_fill,
        label = Res.string.home
    )

    data object NotificationsScreen : Destinations(
        route = "notifications_screen",
        icon = Res.drawable.notifications_outline,
        activeIcon = Res.drawable.notifications,
        label = Res.string.alerts
    )

    data object OwnProfile : Destinations(
        route = "own_profile_screen",
        icon = Res.drawable.bookmark_outline,
        activeIcon = Res.drawable.bookmark_outline,
        label = Res.string.profile
    )

    data object NewPost : Destinations(
        route = "new_post_screen", icon = Res.drawable.add_circle_outline,
        activeIcon = Res.drawable.add_circle
    )

    data object Search : Destinations(
        route = "search_screen",
        icon = Res.drawable.search_outline,
        activeIcon = Res.drawable.search,
        label = Res.string.search
    )

    data object Profile : Destinations(
        route = "profile_screen/{userid}"
    )

    data object ProfileByUsername : Destinations(
        route = "profile_screen/byUsername/{username}"
    )

    data object EditProfile : Destinations(
        route = "edit_profile_screen"
    )

    data object IconSelection : Destinations(
        route = "icon_selection_screen"
    )

    data object MutedAccounts : Destinations(
        route = "muted_accounts_screen"
    )

    data object BlockedAccounts : Destinations(
        route = "blocked_accounts_screen"
    )

    data object LikedPosts : Destinations(
        route = "liked_posts_screen"
    )

    data object BookmarkedPosts : Destinations(
        route = "bookmarked_posts_screen"
    )

    data object FollowedHashtags : Destinations(
        route = "followed_hashtags_screen"
    )

    data object AboutInstance : Destinations(
        route = "about_instance_screen"
    )

    data object AboutPixelix : Destinations(
        route = "about_pixelix_screen"
    )

    data object EditPost : Destinations(
        route = "edit_post_screen/{postId}"
    )

    data object Hashtag : Destinations(
        route = "hashtag_timeline_screen/{hashtag}"
    )

    data object SinglePost : Destinations(
        route = "single_post_screen/{postid}"
    )

    data object Collection : Destinations(
        route = "collection_screen/{collectionid}"
    )

    data object Followers : Destinations(
        route = "followers_screen/{page}/{userid}"
    )



    data object Conversation : Destinations(
        route = "conversations"
    )

    data object Chat : Destinations(
        route = "chat/{userid}"
    )

    data object Mention : Destinations(
        route = "mention/{mentionid}"
    )
}