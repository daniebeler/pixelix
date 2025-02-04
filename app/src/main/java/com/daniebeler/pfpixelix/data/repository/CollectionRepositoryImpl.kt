package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.PostDto
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import com.daniebeler.pfpixelix.utils.execute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val pixelfedApi: PixelfedApi
) : CollectionRepository {

    override fun getCollections(userId: String, page: Int): Flow<Resource<List<Collection>>> {
        return NetworkCall<Collection, CollectionDto>().makeCallList(
            pixelfedApi.getCollectionsByUserId(
                userId, page
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

    override fun removePostOfCollection(
        collectionId: String, postId: String
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.removePostOfCollection(
                collectionId, postId
            ).execute()
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun addPostOfCollection(
        collectionId: String, postId: String
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = pixelfedApi.addPostOfCollection(
                collectionId, postId
            ).execute()
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: "Unknown Error"))
        }
    }

    override fun updateCollection(collectionId: String, title: String, description: String, visibility: String): Flow<Resource<Collection>> {
        return NetworkCall<Collection, CollectionDto>().makeCall(
            pixelfedApi.updateCollection(
                collectionId, title, description, visibility
            )
        )
    }
}