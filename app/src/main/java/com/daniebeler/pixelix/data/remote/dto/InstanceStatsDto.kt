package com.daniebeler.pixelix.data.remote.dto

import com.daniebeler.pixelix.domain.model.InstanceStats
import com.google.gson.annotations.SerializedName

data class InstanceStatsDto(
    @SerializedName("user_count")
    val userCount: Int,
    @SerializedName("status_count")
    val statusCount: Int,
    @SerializedName("domain_count")
    val domainCount: Int
) : DtoInterface<InstanceStats> {
    override fun toModel(): InstanceStats {
        return InstanceStats(
            userCount = userCount,
            statusCount = statusCount,
            domainCount = domainCount
        )
    }
}
