package com.daniebeler.pfpixelix.domain.usecase

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.DirectMessagesRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DeleteMessageUseCase(
    private val directMessagesRepository: DirectMessagesRepository
) {
    operator fun invoke(postId: String): Flow<Resource<List<Int>>> {
        return directMessagesRepository.deleteMessage(postId)
    }
}