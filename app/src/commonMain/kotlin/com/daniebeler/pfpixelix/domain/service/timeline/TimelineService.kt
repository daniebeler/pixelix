package com.daniebeler.pfpixelix.domain.service.timeline

import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class TimelineService(
    private val api: PixelfedApi,
    private val prefs: UserPreferences
) {
    fun getHomeTimeline(maxPostId: String = "", enableReblogs: Boolean = false) =
        loadListResources {
            if (maxPostId.isNotEmpty()) {
                api.getHomeTimeline(maxPostId, enableReblogs)
            } else {
                api.getHomeTimeline(enableReblogs)
            }
        }.filterSensitive()

    fun getLocalTimeline(maxPostId: String = "") = loadListResources {
        if (maxPostId.isNotEmpty()) {
            api.getLocalTimeline(maxPostId)
        } else {
            api.getLocalTimeline()
        }
    }.filterSensitive()

    fun getGlobalTimeline(maxPostId: String = "") = loadListResources {
        if (maxPostId.isNotEmpty()) {
            api.getGlobalTimeline(maxPostId)
        } else {
            api.getGlobalTimeline()
        }
    }.filterSensitive()

    fun getHashtagTimeline(
        hashtag: String,
        maxId: String = "",
        limit: Int = Constants.HASHTAG_TIMELINE_POSTS_LIMIT
    ) = loadListResources {
        if (maxId.isNotEmpty()) {
            api.getHashtagTimeline(hashtag, maxId, limit)
        } else {
            api.getHashtagTimeline(hashtag, limit)
        }
    }.filterSensitive()

    private fun Flow<Resource<List<Post>>>.filterSensitive() = this.map { event ->
        if (event is Resource.Success<List<Post>>) {
            val hideSensitiveContent = prefs.hideSensitiveContent
            val filtered = event.data.filter { !(hideSensitiveContent && it.sensitive) }
            Resource.Success(filtered)
        } else {
            event
        }
    }
}