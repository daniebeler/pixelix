package com.daniebeler.pfpixelix.domain.model

data class AccessToken(
    val accessToken: String,
    val scope: String?,
    val createdAt: String
)