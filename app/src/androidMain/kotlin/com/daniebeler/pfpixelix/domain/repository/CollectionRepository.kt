package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollections(userId: String, page: Int): Flow<Resource<List<Collection>>>
    fun getCollection(collectionId: String): Flow<Resource<Collection>>
    fun getPostsOfCollection(collectionId: String): Flow<Resource<List<Post>>>
    fun removePostOfCollection(collectionId: String, postId: String): Flow<Resource<String>>
    fun addPostOfCollection(collectionId: String, postId: String): Flow<Resource<String>>
    fun updateCollection(collectionId: String, title: String, description: String, visibility: String): Flow<Resource<Collection>>
}