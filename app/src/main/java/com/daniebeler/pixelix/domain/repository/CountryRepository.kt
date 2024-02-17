package com.daniebeler.pixelix.domain.repository

import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.model.AccessToken
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Application
import com.daniebeler.pixelix.domain.model.Instance
import com.daniebeler.pixelix.domain.model.Notification
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.domain.model.Relationship
import com.daniebeler.pixelix.domain.model.Reply
import com.daniebeler.pixelix.domain.model.Search
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun doesAccessTokenExist(): Boolean


    suspend fun storeBaseUrl(url: String)

    fun getBaseUrlFromStorage(): Flow<String>

    suspend fun storeAccessToken(accessToken: String)

    fun getAccessTokenFromStorage(): Flow<String>
    fun setAccessToken(token: String)


    fun getTrendingAccounts(): Flow<Resource<List<Account>>>


    fun getRelationships(userIds: List<String>): Flow<Resource<List<Relationship>>>



    fun getReplies(userid: String, postId: String): Flow<Resource<List<Reply>>>


    fun getInstance(): Flow<Resource<Instance>>


    fun getNotifications(maxNotificationId: String = ""): Flow<Resource<List<Notification>>>


    fun search(searchText: String): Flow<Resource<Search>>


    fun createReply(postId: String, content: String): Flow<Resource<Post>>

    suspend fun createApplication(): Application?

    fun obtainToken(
        clientId: String, clientSecret: String, code: String
    ): Flow<Resource<AccessToken>>

    fun verifyToken(token: String): Flow<Resource<Account>>
}