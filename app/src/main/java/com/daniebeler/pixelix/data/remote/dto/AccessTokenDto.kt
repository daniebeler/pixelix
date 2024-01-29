package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.AccessToken
import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("created_at")
    val createdAt: String
): DtoInterface<AccessToken> {
    override fun toModel(): AccessToken {
        return AccessToken(
            accessToken = accessToken,
            scope = scope,
            createdAt = createdAt
        )
    }
}
