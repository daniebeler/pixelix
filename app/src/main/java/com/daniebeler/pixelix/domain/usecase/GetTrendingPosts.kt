package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingPosts(
    private val repository: CountryRepository
) {
    operator fun invoke(timeRange: String): Flow<Resource<List<Post>>> {
        return repository.getTrendingPosts(timeRange)
    }
}