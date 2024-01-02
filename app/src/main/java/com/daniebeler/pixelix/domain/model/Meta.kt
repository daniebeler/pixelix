package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.MetaDto

data class Meta(
    val original: Original?
)

fun MetaDto.toMeta(): Meta {
    return Meta(
        original = original?.toOriginal()
    )
}