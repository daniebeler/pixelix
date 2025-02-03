package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.LikedBy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikedByDto(
    @SerialName("id") val id: String?,
    @SerialName("others") val others: Boolean,
    @SerialName("total_count") val totalCount: Int?,
    @SerialName("total_count_pretty") val totalCountPretty: String?,
    @SerialName("url") val url: String?,
    @SerialName("username") val username: String?
) : DtoInterface<LikedBy> {
    override fun toModel(): LikedBy {
        return LikedBy(
            id = id,
            username = username,
            others = others,
            totalCount = totalCount ?: 0
        )
    }
}