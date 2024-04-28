package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import com.google.gson.annotations.SerializedName

data class RelatedHashtagDto(
    @SerializedName("name") val name: String, @SerializedName("related_count") val relatedCount: Int
) : DtoInterface<RelatedHashtag> {
    override fun toModel(): RelatedHashtag {
        return RelatedHashtag(
            name = name, relatedCount = relatedCount
        )
    }
}