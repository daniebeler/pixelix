package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.ServerStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerStatsDto(
    @SerialName("monthly_active_users") val monthlyActiveUsers: Int,
    @SerialName("status_count") val statusCount: Int,
    @SerialName("user_count") val userCount: Int
) : DtoInterface<ServerStats> {
    override fun toModel(): ServerStats {
        return ServerStats(
            monthlyActiveUsers = monthlyActiveUsers,
            statusCount = statusCount,
            userCount = userCount
        )
    }
}