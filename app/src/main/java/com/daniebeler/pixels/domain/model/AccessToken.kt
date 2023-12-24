package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.AccessTokenDto
import com.google.gson.annotations.SerializedName

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