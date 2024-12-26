package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.ServerStats
import com.google.gson.annotations.SerializedName

data class ServerStatsDto(
    @SerializedName("monthly_active_users") val monthlyActiveUsers: Int,
    @SerializedName("status_count") val statusCount: Int,
    @SerializedName("user_count") val userCount: Int
) : DtoInterface<ServerStats> {
    override fun toModel(): ServerStats {
        return ServerStats(
            monthlyActiveUsers = monthlyActiveUsers,
            statusCount = statusCount,
            userCount = userCount
        )
    }
}