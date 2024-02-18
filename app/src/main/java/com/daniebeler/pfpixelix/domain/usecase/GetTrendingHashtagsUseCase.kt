package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingHashtagsUseCase(
    private val hashtagRepository: HashtagRepository
) {
    operator fun invoke(): Flow<Resource<List<Tag>>> {
        return hashtagRepository.getTrendingHashtags()
    }
}