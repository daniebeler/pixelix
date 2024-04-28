package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import com.daniebeler.pfpixelix.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface HashtagRepository {

    fun getTrendingHashtags(): Flow<Resource<List<Tag>>>
    fun getFollowedHashtags(): Flow<Resource<List<Tag>>>
    fun getRelatedHashtags(hashtag: String): Flow<Resource<List<RelatedHashtag>>>
    fun getHashtag(hashtag: String): Flow<Resource<Tag>>
    fun followHashtag(tagId: String): Flow<Resource<Tag>>
    fun unfollowHashtag(tagId: String): Flow<Resource<Tag>>
}