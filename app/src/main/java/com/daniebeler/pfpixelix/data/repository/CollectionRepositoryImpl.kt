package com.daniebeler.pfpixelix.data.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.data.remote.PixelfedApi
import com.daniebeler.pfpixelix.data.remote.dto.AccountDto
import com.daniebeler.pfpixelix.data.remote.dto.CollectionDto
import com.daniebeler.pfpixelix.data.remote.dto.RelationshipDto
import com.daniebeler.pfpixelix.data.remote.dto.UpdateAccountDto
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Collection
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.repository.AccountRepository
import com.daniebeler.pfpixelix.domain.repository.CollectionRepository
import com.daniebeler.pfpixelix.utils.NetworkCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.awaitResponse
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
}