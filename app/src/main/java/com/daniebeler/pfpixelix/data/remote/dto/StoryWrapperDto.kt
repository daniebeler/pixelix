package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.StoryWrapper
import com.google.gson.annotations.SerializedName

data class StoryWrapperDto(
    @SerializedName("id") val id: String,
    @SerializedName("nodes") val nodes: List<StoryDto>,
    @SerializedName("seen") val seen: Boolean,
    @SerializedName("url") val url: String,
    @SerializedName("user") val user: StoryUserDto
) : DtoInterface<StoryWrapper> {
    override fun toModel(): StoryWrapper {
        return StoryWrapper(
            id = id,
            nodes = nodes.map { it.toModel() },
            seen = seen,
            url = url,
            user = user.toModel()
        )
    }
}