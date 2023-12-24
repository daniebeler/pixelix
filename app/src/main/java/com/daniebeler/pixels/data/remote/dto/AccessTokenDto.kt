package com.daniebeler.pixels.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("created_at")
    val createdAt: String
)
