package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    @SerialName("focus") val focus: FocusDto?,
    @SerialName("original") val original: OriginalDto?
) : DtoInterface<Meta> {
    override fun toModel(): Meta {
        return Meta(
            original = original?.toModel()
        )
    }
}