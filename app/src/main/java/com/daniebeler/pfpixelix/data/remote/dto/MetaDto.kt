package com.daniebeler.pfpixelix.data.remote.dto

import com.daniebeler.pfpixelix.domain.model.Meta
import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("focus")
    val focus: FocusDto,
    @SerializedName("original")
    val original: OriginalDto?
): DtoInterface<Meta> {
    override fun toModel(): Meta {
        return Meta(
            original = original?.toModel()
        )
    }
}