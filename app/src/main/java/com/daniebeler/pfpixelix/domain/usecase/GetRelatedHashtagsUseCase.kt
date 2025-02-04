package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetRelatedHashtagsUseCase(
    private val hashtagRepository: HashtagRepository
) {
    operator fun invoke(hashtag: String): Flow<Resource<List<RelatedHashtag>>> {
        return hashtagRepository.getRelatedHashtags(hashtag)
    }
}