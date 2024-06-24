package com.daniebeler.pfpixelix.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.daniebeler.pfpixelix.R

sealed class Destinations(
    val route: String, val icon: ImageVector? = null, @StringRes val label: Int = R.string.home
) {
    object HomeScreen : Destinations(
        route = "home_screen", icon = Icons.Outlined.Home, label = R.string.home
    )

    object TrendingScreen : Destinations(
        route = "trending_screen/{page}",
        icon = Icons.AutoMirrored.Outlined.TrendingUp,
        label = R.string.trending
    )

    object NotificationsScreen : Destinations(
        route = "notifications_screen", icon = Icons.Outlined.Notifications, label = R.string.alerts
    )

    object OwnProfile : Destinations(
        route = "own_profile_screen", icon = Icons.Outlined.AccountCircle, label = R.string.profile
    )

    object Profile : Destinations(
        route = "profile_screen/{userid}", icon = Icons.Outlined.Favorite
    )

    object EditProfile : Destinations(
        route = "edit_profile_screen", icon = Icons.Outlined.Settings
    )

    object Preferences : Destinations(
        route = "preferences_screen", icon = Icons.Outlined.Settings
    )

    object IconSelection : Destinations(
        route = "icon_selection_screen", icon = Icons.Outlined.Settings
    )

    object MutedAccounts : Destinations(
        route = "muted_accounts_screen", icon = Icons.Outlined.Settings
    )

    object BlockedAccounts : Destinations(
        route = "blocked_accounts_screen", icon = Icons.Outlined.Settings
    )

    object LikedPosts : Destinations(
        route = "liked_posts_screen", icon = Icons.Outlined.Settings
    )

    object BookmarkedPosts : Destinations(
        route = "bookmarked_posts_screen", icon = Icons.Outlined.Settings
    )

    object FollowedHashtags : Destinations(
        route = "followed_hashtags_screen", icon = Icons.Outlined.Settings
    )

    object AboutInstance : Destinations(
        route = "about_instance_screen", icon = Icons.Outlined.Settings
    )

    object AboutPixelix : Destinations(
        route = "about_pixelix_screen", icon = Icons.Outlined.Settings
    )

    object NewPost : Destinations(
        route = "new_post_screen", icon = Icons.Outlined.Settings
    )

    object Hashtag : Destinations(
        route = "hashtag_timeline_screen/{hashtag}", icon = Icons.Outlined.Settings
    )

    object SinglePost : Destinations(
        route = "single_post_screen/{postid}", icon = Icons.Outlined.Favorite
    )

    object Collection : Destinations(
        route = "collection_screen/{collectionid}", icon = Icons.Outlined.Favorite
    )

    object Followers : Destinations(
        route = "followers_screen/{page}/{userid}", icon = Icons.Outlined.Favorite
    )

    object Search : Destinations(
        route = "search_screen", icon = Icons.Outlined.Search, label = R.string.search
    )

    object Conversation : Destinations(
        route = "conversations", icon = Icons.Outlined.Home
    )

    object Chat : Destinations(
        route = "chat/{userid}", icon = Icons.Outlined.Home
    )
}