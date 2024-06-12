package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.data.remote.dto.StoryCarouselDto
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.StoryCarousel
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.domain.repository.StoryRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : StoryRepository {

    override fun getStoriesCarousel(): Flow<Resource<StoryCarousel>> {
        return NetworkCall<StoryCarousel, StoryCarouselDto>().makeCall(
            pixelfedApi.getStoriesCarousel()
        )
    }
}