package com.daniebeler.pixels.domain.model

import com.daniebeler.pixels.data.remote.dto.LikedByDto

data class LikedBy(
    val totalCount: Int
)

fun LikedByDto.toLikedBy(): LikedBy {
    return LikedBy(
        totalCount = totalCount
    )
}