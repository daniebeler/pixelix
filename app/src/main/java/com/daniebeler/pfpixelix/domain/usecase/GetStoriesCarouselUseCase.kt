package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.StoryCarousel
import com.daniebeler.pfpixelix.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class GetStoriesCarouselUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(): Flow<Resource<StoryCarousel>> {
        return storyRepository.getStoriesCarousel()
    }
}