package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.License
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LicenseDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String
) : DtoInterface<License> {
    override fun toModel(): License {
        return License(
            id = id, title = title, url = url
        )
    }
}