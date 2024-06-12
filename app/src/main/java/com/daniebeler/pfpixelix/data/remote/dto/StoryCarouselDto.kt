package com.daniebeler.pfpixelix.data.remote.dto


import com.daniebeler.pfpixelix.domain.model.StoryCarousel
import com.google.gson.annotations.SerializedName

data class StoryCarouselDto(
    @SerializedName("nodes") val nodes: List<StoryWrapperDto>,
    @SerializedName("self") val self: SelfStoryWrapperDto
) : DtoInterface<StoryCarousel> {
    override fun toModel(): StoryCarousel {
        return StoryCarousel(
            nodes = nodes.map { it.toModel() }, self = self.toModel()
        )
    }
}