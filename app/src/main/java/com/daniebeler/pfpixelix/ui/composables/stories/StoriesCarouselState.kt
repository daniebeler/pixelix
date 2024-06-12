package com.daniebeler.pfpixelix.ui.composables.stories

import com.daniebeler.pfpixelix.domain.model.StoryCarousel

data class StoriesCarouselState(
    val isLoading: Boolean = false, val carousel: StoryCarousel? = null, val error: String = ""
)
