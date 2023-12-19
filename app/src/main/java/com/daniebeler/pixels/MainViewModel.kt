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
import com.daniebeler.pixels.api.models.Account
import com.daniebeler.pixels.api.models.Application
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.api.models.Hashtag
import com.daniebeler.pixels.api.models.Notification
import com.daniebeler.pixels.api.models.Post
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

    private var _dailyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _monthlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _yearlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _trendingHashtags by mutableStateOf(emptyList<Hashtag>())
    private var _localTimeline by mutableStateOf(emptyList<Post>())
    private var _homeTimeline by mutableStateOf(emptyList<Post>())



    private var _ownAccount: Account? by mutableStateOf(null)
    var ownPosts: List<Post> by mutableStateOf(emptyList())
        private set

    var notifications: List<Notification> by mutableStateOf(emptyList())
        private set

    private var _verified: Account? by mutableStateOf(null)

    private var _gotDataFromDataStore = MutableStateFlow(false)
    val gotDataFromDataStore: StateFlow<Boolean> get() = _gotDataFromDataStore.asStateFlow()

    private var _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> get() = _accessToken.asStateFlow()

    init {
        collectTokenFlow()
    }

    var _authApplication: Application? = null

    val dailyTrendingPosts: List<Post>
        get() = _dailyTrendingPosts

    val monthlyTrendingPosts: List<Post>
        get() = _monthlyTrendingPosts

    val yearlyTrendingPosts: List<Post>
        get() = _yearlyTrendingPosts

    val trendingHashtags: List<Hashtag>
        get() = _trendingHashtags

    val localTimeline: List<Post>
        get() = _localTimeline

    val homeTimeline: List<Post>
        get() = _homeTimeline

    val ownAccount: Account?
        get() = _ownAccount

    val verified: Account?
        get() = _verified

    fun getDailyTrendingPosts() {
        viewModelScope.launch {
            _dailyTrendingPosts = repository.getTrendingPosts("daily")
        }
    }

    fun getMonthlyTrendingPosts() {
        viewModelScope.launch {
            _monthlyTrendingPosts = repository.getTrendingPosts("monthly")
        }
    }

    fun getYearlyTrendingPosts() {
        viewModelScope.launch {
            _yearlyTrendingPosts = repository.getTrendingPosts("yearly")
        }
    }

    fun getTrendingHashtags() {
        viewModelScope.launch {
            _trendingHashtags = repository.getTrendingHashtags()
        }
    }

    fun getLocalTimeline() {
        viewModelScope.launch {
            _localTimeline = repository.getLocalTimeline()
        }
    }

    fun getHomeTimeline() {
        viewModelScope.launch {
            _homeTimeline = repository.getHomeTimeline(accessToken.value)
        }
    }

    fun getOwnAccount() {
        viewModelScope.launch {
            _ownAccount = repository.getAccount("497910174831013185")
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
            _verified = repository.verifyToken(accessToken.value)
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