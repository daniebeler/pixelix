package com.daniebeler.pfpixelix.domain.usecase

import android.content.Context
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.PostRepository
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.HideSensitiveContentPrefUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPostsOfAccountUseCase(
    private val postRepository: PostRepository,
    private val context: Context
) {
    operator fun invoke(
        accountId: String, maxPostId: String = "", limit: Int = Constants.PROFILE_POSTS_LIMIT
    ): Flow<Resource<List<Post>>> = flow {
        emit(Resource.Loading())
        val hideSensitiveContent = HideSensitiveContentPrefUtil.isEnable(context)
        postRepository.getPostsByAccountId(accountId, maxPostId, limit).collect { timeline ->
            if (timeline is Resource.Success && hideSensitiveContent) {
                val res: List<Post> = timeline.data?.filter { s -> !s.sensitive } ?: emptyList()
                emit(Resource.Success(res))
            } else {
                emit(timeline)
            }
        }
    }
}