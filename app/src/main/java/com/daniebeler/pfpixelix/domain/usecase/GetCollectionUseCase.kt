package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(accountId: String): Flow<Resource<Collection>> {
        return collectionRepository.getCollection(accountId)
    }
}