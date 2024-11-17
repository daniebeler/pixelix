package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.License
import com.google.gson.annotations.SerializedName

data class LicenseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
) : DtoInterface<License> {
    override fun toModel(): License {
        return License(
            id = id, title = title, url = url
        )
    }
}