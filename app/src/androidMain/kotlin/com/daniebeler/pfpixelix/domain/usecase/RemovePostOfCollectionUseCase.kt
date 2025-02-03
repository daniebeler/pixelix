package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class RemovePostOfCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(collectionId: String, postId: String): Flow<Resource<String>> {
        return collectionRepository.removePostOfCollection(collectionId, postId)
    }
}