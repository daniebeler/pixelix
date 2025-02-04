package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.KmpUri
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
expect class UploadMediaUseCase(
    postEditorRepository: PostEditorRepository
) {
    operator fun invoke(
        url: KmpUri,
        description: String,
        context: KmpContext,
    ): Flow<Resource<MediaAttachment>>
}