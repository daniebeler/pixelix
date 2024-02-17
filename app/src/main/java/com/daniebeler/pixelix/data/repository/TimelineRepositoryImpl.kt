package com.daniebeler.pixelix.data.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.remote.dto.PostDto
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.repository.TimelineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimelineRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
): TimelineRepository {

    override fun getHomeTimeline(maxPostId: String): Flow<Resource<List<Post>>> {
        return if (maxPostId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHomeTimeline(
                    maxPostId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(pixelfedApi.getHomeTimeline())
        }
    }

    override fun getHashtagTimeline(hashtag: String, maxId: String): Flow<Resource<List<Post>>> {
        return if (maxId.isNotEmpty()) {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag, maxId
                )
            )
        } else {
            NetworkCall<Post, PostDto>().makeCallList(
                pixelfedApi.getHashtagTimeline(
                    hashtag
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