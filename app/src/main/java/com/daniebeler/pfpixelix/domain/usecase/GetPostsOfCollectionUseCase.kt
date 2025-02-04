package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetPostsOfCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(collectionId: String): Flow<Resource<List<Post>>> {
        return collectionRepository.getPostsOfCollection(collectionId)
    }
}