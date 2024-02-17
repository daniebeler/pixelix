package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface HashtagRepository {

    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>
    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun followHashtag(tagId: String): Flow<Resource<Tag>>
    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>
}