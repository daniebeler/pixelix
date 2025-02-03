package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.ServerLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerLocationDto(
    @SerialName("city") val city: String?,
    @SerialName("country") val country: String?,
) : DtoInterface<ServerLocation> {
    override fun toModel(): ServerLocation {
        return ServerLocation(
            city = city, country = country
        )
    }
}