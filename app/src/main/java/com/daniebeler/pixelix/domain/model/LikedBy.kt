package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.LikedByDto

data class LikedBy(
    val totalCount: Int
)

fun LikedByDto.toLikedBy(): LikedBy {
    return LikedBy(
        totalCount = totalCount
    )
}