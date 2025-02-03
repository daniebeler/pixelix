package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.RelatedHashtagDto
import com.daniebeler.pfpixelix.data.remote.dto.TagDto
import com.daniebeler.pfpixelix.domain.model.RelatedHashtag
import com.daniebeler.pfpixelix.domain.model.Tag
import com.daniebeler.pfpixelix.domain.repository.HashtagRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class HashtagRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : HashtagRepository {

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> {
        return NetworkCall<Tag, TagDto>().makeCallList(pixelfedApi.getFollowedHashtags())
    }

    override fun getRelatedHashtags(hashtag: String): Flow<Resource<List<RelatedHashtag>>> {
        return NetworkCall<RelatedHashtag, RelatedHashtagDto>().makeCallList(pixelfedApi.getRelatedHashtags(hashtag))
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