package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import android.net.Uri
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.repository.PostEditorRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class UploadMediaUseCase(
    private val postEditorRepository: PostEditorRepository
) {
    operator fun invoke(
        url: Uri,
        description: String,
        context: Context,
    ): Flow<Resource<MediaAttachment>> {
        return postEditorRepository.uploadMedia(
            url, description, context
        )
    }
}