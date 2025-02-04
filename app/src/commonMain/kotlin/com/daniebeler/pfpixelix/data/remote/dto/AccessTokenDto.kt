package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.AccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("created_at") val createdAt: String
) : DtoInterface<AccessToken> {
    override fun toModel(): AccessToken {
        return AccessToken(
            accessToken = accessToken, createdAt = createdAt
        )
    }
}
