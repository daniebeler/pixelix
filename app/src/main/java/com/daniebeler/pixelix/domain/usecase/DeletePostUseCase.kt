package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow

class DeletePostUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(postId: String): Flow<Resource<Post>> {
        return postEditorRepository.deletePost(postId)
    }
}