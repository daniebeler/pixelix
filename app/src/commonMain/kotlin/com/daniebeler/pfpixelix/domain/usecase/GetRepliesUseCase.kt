package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.PostContext
import com.daniebeler.pfpixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetRepliesUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(postId: String): Flow<Resource<PostContext>> {
        return repository.getReplies(postId)
    }
}