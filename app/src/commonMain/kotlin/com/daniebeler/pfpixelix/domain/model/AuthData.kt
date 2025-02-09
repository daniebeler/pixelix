package com.daniebeler.pfpixelix.domain.model

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.service.session.Credentials
import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val loginDataList: List<LoginData> = emptyList(), val currentlyLoggedIn: String = ""
)

@Serializable
data class LoginData(
    val version: Constants.AuthVersions = Constants.AuthVersions.V2,
    val accountId: String = "",
    val username: String = "",
    val displayName: String? = null,
    val followers: Int = 0,
    val avatar: String = "",
    val clientId: String = "",
    val clientSecret: String = "",
    val baseUrl: String = "",
    val accessToken: String = "",
    val loginOngoing: Boolean = false,
)

fun credentialsToAccount(credentials: Credentials): Account {
    return Account(
        username = credentials.username,
        avatar = credentials.avatar,
        url = credentials.serverUrl,
        id = credentials.accountId,
        displayname = credentials.displayName,
        followersCount = 0,
        acct = "",
        note = "",
        locked = false,
        isAdmin = false,
        createdAt = "",
        postsCount = 0,
        followingCount = 0,
        website = null,
        pronouns = emptyList()
    )
}