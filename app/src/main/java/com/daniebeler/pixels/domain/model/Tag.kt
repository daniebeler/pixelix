package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.TagDto

data class Tag(
    var name: String,
    val url: String,
    val following: Boolean,
    val count: Int
)

fun TagDto.toTag(): Tag {
    return Tag(
        name = name,
        url = url,
        following = following,
        count = count
    )
}