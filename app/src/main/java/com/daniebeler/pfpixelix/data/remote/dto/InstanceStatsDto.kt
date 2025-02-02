package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.InstanceStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstanceStatsDto(
    @SerialName("user_count") val userCount: Int,
    @SerialName("status_count") val statusCount: Int,
    @SerialName("domain_count") val domainCount: Int
) : DtoInterface<InstanceStats> {
    override fun toModel(): InstanceStats {
        return InstanceStats(
            userCount = userCount, statusCount = statusCount, domainCount = domainCount
        )
    }
}
