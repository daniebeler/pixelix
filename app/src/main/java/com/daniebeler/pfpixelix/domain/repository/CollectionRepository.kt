package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Collection
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollections(userId: String): Flow<Resource<List<Collection>>>
}