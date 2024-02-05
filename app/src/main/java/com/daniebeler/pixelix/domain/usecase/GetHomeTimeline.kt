package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetHomeTimeline(
    private val repository: CountryRepository
) {
    operator fun invoke(maxPostId: String = ""): Flow<Resource<List<Post>>> {
        return repository.getHomeTimeline(maxPostId)
    }
}