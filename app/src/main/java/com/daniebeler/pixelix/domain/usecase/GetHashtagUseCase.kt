package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.HashtagRepository
import kotlinx.coroutines.flow.Flow

class GetHashtagUseCase(
    private val hashtagRepository: HashtagRepository
) {
    operator fun invoke(hashtag: String): Flow<Resource<Tag>> {
        return hashtagRepository.getHashtag(hashtag)
    }
}