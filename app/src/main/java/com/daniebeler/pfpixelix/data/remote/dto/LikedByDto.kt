package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.LikedBy
import com.google.gson.annotations.SerializedName

data class LikedByDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("others")
    val others: Boolean,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("total_count_pretty")
    val totalCountPretty: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("username")
    val username: String
) : DtoInterface<LikedBy> {
    override fun toModel(): LikedBy {
        return LikedBy(
            id = id,
            username = username,
            others = others,
            totalCount = totalCount
        )
    }
}