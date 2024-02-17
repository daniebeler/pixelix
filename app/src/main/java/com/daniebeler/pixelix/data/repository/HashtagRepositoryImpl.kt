package com.daniebeler.pixelix.data.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.data.remote.PixelfedApi
import com.daniebeler.pixelix.data.remote.dto.TagDto
import com.daniebeler.pixelix.domain.model.Tag
import com.daniebeler.pixelix.domain.repository.HashtagRepository
import com.daniebeler.pixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HashtagRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : HashtagRepository {

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getFollowedHashtags())
    }

    override fun getTrendingHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getTrendingHashtags())
    }

    override fun getHashtag(hashtag: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.getHashtag(hashtag))
    }

    override fun followHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.followHashtag(tagId))
    }

    override fun unfollowHashtag(tagId: String): Flow<Resource<Tag>> {
        return NetworkCall<Tag, TagDto>().makeCall(pixelfedApi.unfollowHashtag(tagId))
    }
}