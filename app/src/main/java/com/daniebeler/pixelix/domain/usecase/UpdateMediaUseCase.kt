package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.MediaAttachment
import com.daniebeler.pixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow

class UpdateMediaUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(id: String, description: String): Flow<Resource<MediaAttachment>> {
        return postEditorRepository.updateMedia(id, description)
    }
}