package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.TagDto

data class Tag(
    val name: String,
    val url: String
)

fun TagDto.toTag(): Tag {
    return Tag(
        name = name,
        url = url
    )
}