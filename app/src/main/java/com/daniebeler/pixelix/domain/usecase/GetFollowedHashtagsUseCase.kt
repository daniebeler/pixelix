package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.HashtagRepository
import kotlinx.coroutines.flow.Flow

class GetFollowedHashtagsUseCase(
    private val hashtagRepository: HashtagRepository
) {
    operator fun invoke(): Flow<Resource<List<Tag>>> {
        return hashtagRepository.getFollowedHashtags()
    }
}