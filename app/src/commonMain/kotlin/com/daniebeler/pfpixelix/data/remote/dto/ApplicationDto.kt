package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApplicationDto(
    @SerialName("name") val name: String?,
    @SerialName("website") val website: JsonElement?,
    @SerialName("id") val id: String?,
    @SerialName("redirect_uri") val redirectUri: String?,
    @SerialName("client_id") val clientId: String?,
    @SerialName("client_secret") val clientSecret: String?
) : DtoInterface<Application> {
    override fun toModel(): Application {
        return Application(
            name = name ?: "",
            id = id ?: "",
            redirectUri = redirectUri ?: "",
            clientId = clientId ?: "",
            clientSecret = clientSecret ?: ""
        )
    }
}