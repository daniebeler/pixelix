package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject


@Inject
class UpdateCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(
        collectionId: String, title: String, description: String, visibility: String
    ): Flow<Resource<Collection>> {
        return collectionRepository.updateCollection(collectionId, title, description, visibility)
    }
}

