package com.daniebeler.pixelix.common

object Constants {
    const val CLIENT_ID_DATASTORE_KEY = "client_id"
    const val CLIENT_SECRET_DATASTORE_KEY = "client_secret"
    const val ACCESS_TOKEN_DATASTORE_KEY = "access_token"
    const val ACCOUNT_ID_DATASTORE_KEY = "account_id"
    const val BASE_URL_DATASTORE_KEY = "base_url"

    const val HASHTAG_TIMELINE_POSTS_LIMIT = 8
    const val HOME_TIMELINE_POSTS_LIMIT = 8
    const val LOCAL_TIMELINE_POSTS_LIMIT = 8
    const val GLOBAL_TIMELINE_POSTS_LIMIT = 8
    const val NOTIFICATIONS_LIMIT = 30
    const val LIKED_POSTS_LIMIT = 40
    const val PROFILE_POSTS_LIMIT = 18
    const val LIKED_BY_LIMIT = 40

    const val AUDIENCE_PUBLIC = "public"
    const val AUDIENCE_UNLISTED = "unlisted"
    const val AUDIENCE_FOLLOWERS_ONLY = "private"
}