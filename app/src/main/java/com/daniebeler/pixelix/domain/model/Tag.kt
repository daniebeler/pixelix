package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.TagDto

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