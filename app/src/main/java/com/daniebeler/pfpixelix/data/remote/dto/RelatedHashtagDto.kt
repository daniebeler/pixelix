package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RelatedHashtagDto(
    @SerialName("name") val name: String,
    @SerialName("related_count") val relatedCount: Int
) : DtoInterface<RelatedHashtag> {
    override fun toModel(): RelatedHashtag {
        return RelatedHashtag(
            name = name, relatedCount = relatedCount
        )
    }
}