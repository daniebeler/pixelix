package com.daniebeler.pixels

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Application
import com.daniebeler.pixels.domain.model.Notification
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Relationship
import com.daniebeler.pixels.domain.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository,
    application: android.app.Application
): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<android.app.Application>().applicationContext

    private val settingsDataStore = context.dataStore

    var dailyTrendingPosts: List<Post> by mutableStateOf(emptyList())
    var monthlyTrendingPosts: List<Post> by mutableStateOf(emptyList())
    var yearlyTrendingPosts: List<Post> by mutableStateOf(emptyList())
    var trendingHashtags: List<Tag> by mutableStateOf(emptyList())


    var localTimeline: List<Post> by mutableStateOf(emptyList())

    var ownAccount: Account? by mutableStateOf(null)
    var ownPosts: List<Post> by mutableStateOf(emptyList())

    var notifications: List<Notification> by mutableStateOf(emptyList())

    var homeTimeline: List<Post> by mutableStateOf(emptyList())

    var verified: Account? by mutableStateOf(null)

    private var _gotDataFromDataStore = MutableStateFlow(false)
    val gotDataFromDataStore: StateFlow<Boolean> get() = _gotDataFromDataStore.asStateFlow()

    private var _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> get() = _accessToken.asStateFlow()

    init {
        collectTokenFlow()
    }

    var _authApplication: Application? = null

    fun getDailyTrendingPosts() {
        viewModelScope.launch {
            dailyTrendingPosts = repository.getTrendingPosts("daily")
        }
    }

    fun getMonthlyTrendingPosts() {
        viewModelScope.launch {
            monthlyTrendingPosts = repository.getTrendingPosts("monthly")
        }
    }

    fun getYearlyTrendingPosts() {
        viewModelScope.launch {
            yearlyTrendingPosts = repository.getTrendingPosts("yearly")
        }
    }

    fun getTrendingHashtags() {
        viewModelScope.launch {
            trendingHashtags = repository.getTrendingHashtags()
        }
    }

    suspend fun returnHashtagTimeline(hashtag: String): List<Post> {
        return repository.getHashtagTimeline(hashtag)
    }

    suspend fun returnRelationships(userId: String): List<Relationship> {
        return repository.getRelationships(userId)
    }

    suspend fun returnMutalFollowers(userId: String): List<Account> {
        return repository.getMutalFollowers(userId)
    }

    suspend fun returnAccount(userId: String): Account? {
        return repository.getAccount(userId)
    }

    suspend fun followAccount(userId: String): Relationship? {
        return repository.followAccount(userId)
    }

    suspend fun unfollowAccount(userId: String): Relationship? {
        return repository.unfollowAccount(userId)
    }

    fun getLocalTimeline() {
        viewModelScope.launch {
            localTimeline = repository.getLocalTimeline()
        }
    }

    fun getHomeTimeline() {
        viewModelScope.launch {
            homeTimeline = repository.getHomeTimeline()
        }
    }

    fun getOwnAccount() {
        viewModelScope.launch {
            ownAccount = repository.getAccount("497910174831013185")
        }
    }

    fun getOwnPosts() {
        viewModelScope.launch {
            ownPosts = repository.getPostsByAccountId("497910174831013185")
        }
    }

    fun getNotifications() {
        viewModelScope.launch {
            notifications = repository.getNotifications()
        }
    }

    fun getMoreOwnPosts(maxPostId: String) {
        viewModelScope.launch {
            ownPosts += repository.getPostsByAccountId("497910174831013185", maxPostId)
        }
    }

    fun checkToken() {
        viewModelScope.launch {
            verified = repository.verifyToken(accessToken.value)
        }
    }

    suspend fun registerApplication(): String {
        _authApplication = repository.createApplication()
        if (_authApplication != null) {
            storeClientId(_authApplication!!.clientId)
            storeClientSecret(_authApplication!!.clientSecret)
            return _authApplication!!.clientId
        }
        return ""
    }

    suspend fun obtainToken(code: String): Boolean {
        val clientId: String = getClientIdFromStorage().first()
        val clientSecret: String = getClientSecretFromStorage().first()

        val token = repository.obtainToken(clientId, clientSecret, code)

        if (token != null) {
            storeAccessToken(token.accessToken)
            return true
        }

        return false
    }


    suspend fun storeClientId(clientId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey("client_id")] = clientId
        }
    }

    fun getClientIdFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey("client_id")] ?: ""
    }

    suspend fun storeClientSecret(clientSecret: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey("client_secret")] = clientSecret
        }
    }

    fun getClientSecretFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey("client_secret")] ?: ""
    }

    suspend fun storeAccessToken(accessToken: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey("access_token")] = accessToken
        }
    }

    fun getAccessTokenFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey("access_token")] ?: ""
    }

    private fun collectTokenFlow() {
        viewModelScope.launch {
            getAccessTokenFromStorage().collect {token ->
                repository.setAccessToken(token)
                _accessToken.update {
                    token
                }
                repository.setBaseUrl("https://pixelfed.social/")
                _gotDataFromDataStore.update {
                    (_accessToken.value.isNotEmpty())
                }
            }
        }
    }

    suspend fun logout() {
        storeAccessToken("")
        storeClientId("")
        storeClientSecret("")

    }

}