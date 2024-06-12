package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.StoryCarousel
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStoriesCarousel(): Flow<Resource<StoryCarousel>>
}