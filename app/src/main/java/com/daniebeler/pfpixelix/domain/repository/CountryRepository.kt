package com.daniebeler.pfpixelix.domain.repository

import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.AccessToken
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Application
import com.daniebeler.pfpixelix.domain.model.Instance
import com.daniebeler.pfpixelix.domain.model.nodeinfo.NodeInfo
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Reply
import com.daniebeler.pfpixelix.domain.model.Search
import com.daniebeler.pfpixelix.domain.model.WellKnownDomains
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun getAuthV1Token(): Flow<String>
    fun getAuthV1Baseurl(): Flow<String>
    suspend fun deleteAuthV1Data()

    fun getTrendingAccounts(): Flow<Resource<List<Account>>>

    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>

    fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>>


    fun getInstance(): Flow<Resource<Instance>>


    fun getNotifications(maxNotificationId: String = ""): Flow<Resource<List<Notification>>>


    fun search(searchText: String, type: String?): Flow<Resource<Search>>


    fun createReply(postId: String, content: String): Flow<Resource<Post>>

    suspend fun createApplication(): Application?

    fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): Flow<Resource<AccessToken>>

    fun verifyToken(token: String): Flow<Resource<Account>>

    fun getWellKnownDomains(domain: String): Flow<Resource<WellKnownDomains>>

    fun getNodeInfo(domain: String): Flow<Resource<NodeInfo>>

}