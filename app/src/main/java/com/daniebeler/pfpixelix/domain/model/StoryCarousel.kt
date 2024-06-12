package com.daniebeler.pfpixelix.domain.model

data class StoryCarousel(
    val nodes: List<StoryWrapper>,
    val self: SelfStoryWrapper
)
