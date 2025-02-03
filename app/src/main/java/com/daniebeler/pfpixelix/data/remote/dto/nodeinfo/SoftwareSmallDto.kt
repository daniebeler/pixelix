package com.daniebeler.pfpixelix.data.remote.dto.nodeinfo


import android.util.Log
import com.daniebeler.pfpixelix.data.remote.dto.DtoInterface
import com.daniebeler.pfpixelix.domain.model.nodeinfo.SoftwareSmall
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SoftwareSmallDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("version") val version: String
): DtoInterface<SoftwareSmall> {
    override fun toModel(): SoftwareSmall {
        Log.d("SoftwareSmallDto", "Converting SoftwareSmallDto to SoftwareSmall: $this")
        return SoftwareSmall(
            id = id,
            name = name,
            url = url,
            version = version
        )
    }
}