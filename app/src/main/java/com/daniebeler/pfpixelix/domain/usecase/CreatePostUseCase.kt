package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.CreatePostDto
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class CreatePostUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(post: CreatePostDto): Flow<Resource<Post>> {
        return postEditorRepository.createPost(post)
    }
}