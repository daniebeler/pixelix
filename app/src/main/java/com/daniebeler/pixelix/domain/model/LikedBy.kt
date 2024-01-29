package com.daniebeler.pixelix.domain.model

import com.daniebeler.pixelix.data.remote.dto.LikedByDto

data class LikedBy(
    val id: String?,
    val username: String?,
    val others: Boolean,
    val totalCount: Int
)