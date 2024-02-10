package com.daniebeler.pixelix.domain.usecase

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetHashtagTimelineUseCase(
    private val repository: CountryRepository, private val storageRepository: StorageRepository
) {
    operator fun invoke(hashtag: String, maxPostId: String = ""): Flow<Resource<List<Post>>> =
        flow {
            emit(Resource.Loading())
            val hideSensitiveContent = storageRepository.getHideSensitiveContent().first()
            repository.getHashtagTimeline(hashtag, maxPostId).collect { timeline ->
                if (timeline is Resource.Success && hideSensitiveContent) {
                    val res: List<Post> = timeline.data?.filter { s -> !s.sensitive } ?: emptyList()
                    emit(Resource.Success(res))
                } else {
                    emit(timeline)
                }
            }
        }
}