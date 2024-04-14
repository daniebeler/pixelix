package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : CollectionRepository {

    override fun getCollections(userId: String): Flow<Resource<List<Collection>>> {
        return NetworkCall<Collection, CollectionDto>().makeCallList(
            pixelfedApi.getCollectionsByUserId(
                userId
            )
        )
    }

    override fun getCollection(collectionId: String): Flow<Resource<Collection>> {
        return NetworkCall<Collection, CollectionDto>().makeCall(
            pixelfedApi.getCollection(
                collectionId
            )
        )
    }

    override fun getPostsOfCollection(collectionId: String): Flow<Resource<List<Post>>> {
        return NetworkCall<Post, PostDto>().makeCallList(
            pixelfedApi.getPostsOfCollection(
                collectionId
            )
        )
    }
}