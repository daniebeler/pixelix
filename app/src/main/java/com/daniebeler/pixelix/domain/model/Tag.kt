package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.TagDto

data class Tag(
    var name: String,
    val url: String,
    val following: Boolean,
    var count: Int?,
    val total: Int,
    val hashtag: String?
)

fun TagDto.toTag(): Tag {
    return Tag(
        name = name,
        url = url,
        following = following,
        count = count,
        total = total,
        hashtag = hashtag
    )
}