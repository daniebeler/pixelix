package com.daniebeler.pfpixelix.domain.service.collection

import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import com.daniebeler.pfpixelix.domain.service.utils.loadType
import me.tatarka.inject.annotations.Inject

@Inject
class CollectionService(
    private val api: PixelfedApi
) {

    fun getCollections(userId: String, page: Int) = loadListResources {
        api.getCollectionsByUserId(userId, page)
    }

    fun getCollection(collectionId: String) = loadResource {
        api.getCollection(collectionId)
    }

    fun getPostsOfCollection(collectionId: String) = loadListResources {
        api.getPostsOfCollection(collectionId)
    }

    fun removePostOfCollection(collectionId: String, postId: String) = loadType {
        api.removePostOfCollection(collectionId, postId)
    }

    fun addPostOfCollection(collectionId: String, postId: String) = loadType {
        api.addPostOfCollection(collectionId, postId)
    }

    fun updateCollection(
        collectionId: String,
        title: String,
        description: String,
        visibility: String
    ) = loadResource {
        api.updateCollection(collectionId, title, description, visibility)
    }
}