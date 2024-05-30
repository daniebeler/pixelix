package com.daniebeler.pfpixelix.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthData (
    val loginDataList: List<LoginData> = emptyList(),
    val currentlyLoggedIn: String = ""
)

@Serializable
data class LoginData (
    val accountId: String = "",
    val clientId: String = "",
    val clientSecret: String = "",
    val baseUrl: String = "",
    val accessToken: String = "",
    val loginOngoing: Boolean = false
)