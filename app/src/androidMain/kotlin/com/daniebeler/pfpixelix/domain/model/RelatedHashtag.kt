package com.daniebeler.pfpixelix.domain.model

import com.daniebeler.pfpixelix.data.remote.dto.MessageDto

data class RelatedHashtag (
    val name: String,
    val relatedCount: Int
)