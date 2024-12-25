package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo

import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import com.google.gson.annotations.SerializedName

data class FediSoftwareDto(
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("instance_count") val instanceCount: Int?,
    @SerializedName("license") val license: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("status_count") val statusCount: Int?,
    @SerializedName("user_count") val userCount: Int?
) : DtoInterface<FediSoftware> {
    override fun toModel(): FediSoftware {
        return FediSoftware(
            id = id,
            description = description ?: "",
            instanceCount = instanceCount ?: -1,
            license = license ?: "",
            name = name ?: "",
            statusCount = statusCount ?: -1,
            userCount = userCount ?: -1,
            slug = slug ?: "",
            icon = null
        )
    }
}