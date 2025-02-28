package com.daniebeler.pfpixelix.common

object Constants {
    const val HASHTAG_TIMELINE_POSTS_LIMIT = 20
    const val HOME_TIMELINE_POSTS_LIMIT = 20
    const val LOCAL_TIMELINE_POSTS_LIMIT = 20
    const val GLOBAL_TIMELINE_POSTS_LIMIT = 20
    const val NOTIFICATIONS_LIMIT = 40
    const val LIKED_POSTS_LIMIT = 40
    const val PROFILE_POSTS_LIMIT = 18
    const val LIKED_BY_LIMIT = 40
    const val FOLLOWERS_LIMIT = 40
    const val BOOKMARKED_LIMIT = 12

    const val AUDIENCE_PUBLIC = "public"
    const val AUDIENCE_UNLISTED = "unlisted"
    const val AUDIENCE_FOLLOWERS_ONLY = "private"

    enum class AuthVersions {
        V1, V2
    }
}