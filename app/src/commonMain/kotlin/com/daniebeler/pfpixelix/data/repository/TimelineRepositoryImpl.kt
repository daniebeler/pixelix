package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.TimelineRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class TimelineRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
): TimelineRepository {

    override fun getHomeTimeline(maxPostId: String, enableReblogs: Boolean): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHomeTimeline(
                    maxPostId, enableReblogs
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getHomeTimeline(enableReblogs))
        }
    }

    override fun getHashtagTimeline(hashtag: String, maxId: String, limit: Int): Flow<Resource<List<Post>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag, maxId, limit
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag, limit
                )
            )
        }
    }

    override fun getLocalTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getLocalTimeline(
                    maxPostId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getLocalTimeline())
        }
    }

    override fun getGlobalTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getGlobalTimeline(
                    maxPostId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getGlobalTimeline())
        }
    }
}