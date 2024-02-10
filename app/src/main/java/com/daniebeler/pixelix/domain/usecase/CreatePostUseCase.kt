package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class CreatePostUseCase(
    private val repository: CountryRepository
) {
    operator fun invoke(post: CreatePostDto): Flow<Resource<Post>> {
        return repository.createPost(post)
    }
}