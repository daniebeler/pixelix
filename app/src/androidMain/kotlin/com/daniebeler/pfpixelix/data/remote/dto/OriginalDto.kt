package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Original
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OriginalDto(
    @SerialName("aspect") val aspect: Double,
    @SerialName("height") val height: Int?,
    @SerialName("size") val size: String?,
    @SerialName("width") val width: Int?
) : DtoInterface<Original> {
    override fun toModel(): Original {
        return Original(
            aspect = aspect
        )
    }
}