package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.SelfStoryWrapper
import com.google.gson.annotations.SerializedName

data class SelfStoryWrapperDto(
    @SerializedName("nodes") val nodes: List<StoryDto>,
    @SerializedName("user") val user: StoryUserDto
) : DtoInterface<SelfStoryWrapper> {
    override fun toModel(): SelfStoryWrapper {
        return SelfStoryWrapper(
            nodes = nodes.map { it.toModel() }, user = user.toModel()
        )
    }
}