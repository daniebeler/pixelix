package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo

import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.FediSoftware
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FediSoftwareDto(
    @SerialName("description") val description: String?,
    @SerialName("id") val id: Int,
    @SerialName("instance_count") val instanceCount: Int?,
    @SerialName("license") val license: String?,
    @SerialName("name") val name: String?,
    @SerialName("slug") val slug: String?,
    @SerialName("status_count") val statusCount: Int?,
    @SerialName("user_count") val userCount: Int?,
    @SerialName("monthly_actives") val activeUserCount: Int?,
    @SerialName("website") val website: String?,
) : DtoInterface<FediSoftware> {
    override fun toModel(): FediSoftware {
        return FediSoftware(
            id = id,
            description = description ?: "",
            instanceCount = instanceCount ?: 0,
            license = license ?: "",
            name = name ?: "",
            statusCount = statusCount ?: 0,
            userCount = userCount ?: 0,
            activeUserCount = activeUserCount ?: 0,
            slug = slug ?: "",
            icon = null,
            website = website ?: ""
        )
    }
}