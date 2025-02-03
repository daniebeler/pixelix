package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class CreateReplyUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String, content: String): Flow<Resource<Post>> {
        return repository.createReply(postId, content)
    }
}