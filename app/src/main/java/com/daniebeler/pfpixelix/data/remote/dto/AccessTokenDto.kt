package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("created_at") val createdAt: String
) : DtoInterface<AccessToken> {
    override fun toModel(): AccessToken {
        return AccessToken(
            accessToken = accessToken, createdAt = createdAt
        )
    }
}
