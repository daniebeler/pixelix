package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.Story
import com.google.gson.annotations.SerializedName

data class StoryDto(
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: String,
    @SerializedName("pid") val pid: String?,
    @SerializedName("seen") val seen: Boolean,
    @SerializedName("src") val src: String,
    @SerializedName("type") val type: String
) : DtoInterface<Story> {
    override fun toModel(): Story {
        return Story(
            createdAt = createdAt,
            duration = duration,
            id = id,
            seen = seen,
            src = src,
            type = type
        )
    }
}