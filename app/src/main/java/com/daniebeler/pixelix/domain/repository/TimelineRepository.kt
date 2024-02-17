package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {

    fun getHomeTimeline(maxPostId: String = ""): Flow<Resource<List<Post>>>
}