package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateMediaUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(id: String, description: String): Flow<Resource<MediaAttachment>> {
        return postEditorRepository.updateMedia(id, description)
    }
}