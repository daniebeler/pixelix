package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.MetaDto

data class Meta(
    val original: Original
)

fun MetaDto.toMeta(): Meta {
    return Meta(
        original = original.toOriginal()
    )
}