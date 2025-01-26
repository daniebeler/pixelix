package com.daniebeler.pfpixelix.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.daniebeler.pfpixelix.R

sealed class Destinations(
    val route: String,
    val icon: ImageVector? = null,
    val activeIcon: ImageVector? = null,
    @StringRes val label: Int = R.string.home
) {
    data object HomeScreen : Destinations(
        route = "home_screen",
        icon = Icons.Outlined.Home,
        activeIcon = Icons.Filled.Home,
        label = R.string.home
    )

    data object TrendingScreen : Destinations(
        route = "trending_screen/{page}",
        icon = Icons.AutoMirrored.Rounded.TrendingUp,
        activeIcon = Icons.AutoMirrored.Rounded.TrendingUp,
        label = R.string.trending
    )

    data object NotificationsScreen : Destinations(
        route = "notifications_screen",
        icon = Icons.Outlined.FavoriteBorder,
        activeIcon = Icons.Filled.Favorite,
        label = R.string.alerts
    )

    data object OwnProfile : Destinations(
        route = "own_profile_screen",
        icon = Icons.Outlined.AccountCircle,
        activeIcon = Icons.Filled.AccountCircle,
        label = R.string.profile
    )

    data object Profile : Destinations(
        route = "profile_screen/{userid}", icon = Icons.Outlined.Favorite,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object ProfileByUsername : Destinations(
        route = "profile_screen/byUsername/{username}", icon = Icons.Outlined.Favorite,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object EditProfile : Destinations(
        route = "edit_profile_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Preferences : Destinations(
        route = "preferences_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object IconSelection : Destinations(
        route = "icon_selection_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object MutedAccounts : Destinations(
        route = "muted_accounts_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object BlockedAccounts : Destinations(
        route = "blocked_accounts_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object LikedPosts : Destinations(
        route = "liked_posts_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object BookmarkedPosts : Destinations(
        route = "bookmarked_posts_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object FollowedHashtags : Destinations(
        route = "followed_hashtags_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object AboutInstance : Destinations(
        route = "about_instance_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object AboutPixelix : Destinations(
        route = "about_pixelix_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object NewPost : Destinations(
        route = "new_post_screen", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object EditPost : Destinations(
        route = "edit_post_screen/{postId}", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Hashtag : Destinations(
        route = "hashtag_timeline_screen/{hashtag}", icon = Icons.Outlined.Settings,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object SinglePost : Destinations(
        route = "single_post_screen/{postid}", icon = Icons.Outlined.Favorite,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Collection : Destinations(
        route = "collection_screen/{collectionid}", icon = Icons.Outlined.Favorite,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Followers : Destinations(
        route = "followers_screen/{page}/{userid}", icon = Icons.Outlined.Favorite,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Search : Destinations(
        route = "search_screen",
        icon = Icons.Outlined.Search,
        activeIcon = Icons.Filled.Search,
        label = R.string.search
    )

    data object Conversation : Destinations(
        route = "conversations", icon = Icons.Outlined.Home,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Chat : Destinations(
        route = "chat/{userid}", icon = Icons.Outlined.Home,
        activeIcon = Icons.Filled.AccountCircle
    )

    data object Mention : Destinations(
        route = "mention/{mentionid}", icon = Icons.Outlined.Home,
        activeIcon = Icons.Filled.AccountCircle
    )
}