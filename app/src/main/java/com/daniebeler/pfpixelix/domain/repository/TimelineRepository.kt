package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {

    fun getHomeTimeline(maxPostId: String = "", enableReblogs: Boolean): Flow<Resource<List<Post>>>
    fun getHashtagTimeline(hashtag: String, maxId: String = "", limit: Int): Flow<Resource<List<Post>>>
    fun getLocalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
    fun getGlobalTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
}