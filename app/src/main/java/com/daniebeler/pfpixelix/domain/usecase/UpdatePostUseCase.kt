package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.dto.UpdatePostDto
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class UpdatePostUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(postId: String, updatePostDto: UpdatePostDto): Flow<Resource<Post?>> {
        return postEditorRepository.updatePost(postId, updatePostDto)
    }
}