package com.daniebeler.pfpixelix.common

object Constants {
    const val CLIENT_ID_DATASTORE_KEY = "client_id"
    const val CLIENT_SECRET_DATASTORE_KEY = "client_secret"
    const val ACCESS_TOKEN_DATASTORE_KEY = "access_token"
    const val ACCOUNT_ID_DATASTORE_KEY = "account_id"
    const val BASE_URL_DATASTORE_KEY = "base_url"
    const val SHOW_SENSITIVE_CONTENT_DATASTORE_KEY = "show_sensitive_content"
    const val SHOW_ALT_TEXT_BUTTON = "show_alt_text_button"
    const val USE_IN_APP_BROWSER_DATASTORE_KEY = "use_in_app_browser"
    const val VOLUME_DATASTORE_KEY = "volume"
    const val VIEW_DATASTORE_KEY = "view"
    const val THEME_DATASTORE_KEY = "theme"

    const val HASHTAG_TIMELINE_POSTS_LIMIT = 20
    const val HOME_TIMELINE_POSTS_LIMIT = 20
    const val LOCAL_TIMELINE_POSTS_LIMIT = 20
    const val GLOBAL_TIMELINE_POSTS_LIMIT = 20
    const val NOTIFICATIONS_LIMIT = 40
    const val LIKED_POSTS_LIMIT = 40
    const val PROFILE_POSTS_LIMIT = 18
    const val LIKED_BY_LIMIT = 40

    const val AUDIENCE_PUBLIC = "public"
    const val AUDIENCE_UNLISTED = "unlisted"
    const val AUDIENCE_FOLLOWERS_ONLY = "private"

    enum class AuthVersions {
        V1, V2
    }
}