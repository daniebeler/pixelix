package com.daniebeler.pfpixelix.domain.service.hashtag

import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class HashtagService(
    private val api: PixelfedApi
) {
    fun getTrendingHashtags() = loadListResources {
        api.getTrendingHashtags()
    }

    fun getFollowedHashtags() = loadListResources {
        api.getFollowedHashtags()
    }

    fun getRelatedHashtags(hashtag: String) = loadListResources {
        api.getRelatedHashtags(hashtag)
    }

    fun getHashtag(hashtag: String) = loadResource {
        api.getHashtag(hashtag)
    }

    fun followHashtag(tagId: String) = loadResource {
        api.followHashtag(tagId)
    }

    fun unfollowHashtag(tagId: String) = loadResource {
        api.unfollowHashtag(tagId)
    }
}