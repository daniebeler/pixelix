package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.ServerLocation
import com.daniebeler.pfpixelix.domain.model.nodeinfo.ServerStats
import com.google.gson.annotations.SerializedName

data class ServerLocationDto(
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?,
) : DtoInterface<ServerLocation> {
    override fun toModel(): ServerLocation {
        return ServerLocation(
            city = city, country = country
        )
    }
}