package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.AccessTokenDto

data class AccessToken(
    val accessToken: String,
    val scope: String,
    val createdAt: String
)

fun AccessTokenDto.toAccessToken(): AccessToken {
    return AccessToken(
        accessToken = accessToken,
        scope = scope,
        createdAt = createdAt
    )
}