package com.daniebeler.pfpixelix.domain.model

import com.daniebeler.pfpixelix.common.Constants
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

fun loginDataToAccount(loginData: LoginData): Account {
    return Account(
        username = loginData.username,
        avatar = loginData.avatar,
        url = loginData.baseUrl,
        id = loginData.accountId,
        displayname = loginData.displayName,
        followersCount = loginData.followers,
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