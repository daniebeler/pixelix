package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.HideSensitiveContentPrefUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHomeTimelineUseCase(
    private val timelineRepository: TimelineRepository,
    private val context: Context
) {
    operator fun invoke(maxPostId: String = "", enableReblogs: Boolean = false): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())
        val hideSensitiveContent = HideSensitiveContentPrefUtil.isEnable(context)
        timelineRepository.getHomeTimeline(maxPostId, enableReblogs).collect { timeline ->
            if (timeline is Resource.Success && hideSensitiveContent) {
                val res: List<Post> = timeline.data?.filter { s -> !s.sensitive } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(timeline)
            }
        }
    }
}