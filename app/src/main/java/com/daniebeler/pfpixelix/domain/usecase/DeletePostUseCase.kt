package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DeletePostUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(postId: String): Flow<Resource<Post>> {
        return postEditorRepository.deletePost(postId)
    }
}