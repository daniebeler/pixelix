package com.daniebeler.pixels

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.models.api.Application
import com.daniebeler.pixels.models.api.CountryRepository
import com.daniebeler.pixels.models.api.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository,
    application: android.app.Application
): AndroidViewModel(application) {

    //private val application.dataStore by preferencesDataStore("app_preferences")
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<android.app.Application>().applicationContext

    private val settingsDataStore = context.dataStore

    private var _dailyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _monthlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _yearlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _localTimeline by mutableStateOf(emptyList<Post>())

    var _authApplication: Application? = null

    val dailyTrendingPosts: List<Post>
        get() = _dailyTrendingPosts

    val monthlyTrendingPosts: List<Post>
        get() = _monthlyTrendingPosts

    val yearlyTrendingPosts: List<Post>
        get() = _yearlyTrendingPosts

    val localTimeline: List<Post>
        get() = _localTimeline

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

    fun getLocalTimeline() {
        viewModelScope.launch {
            _localTimeline = repository.getLocalTimeline()
        }
    }

    fun registerApplication() {
        viewModelScope.launch {
            _authApplication = repository.createApplication()
            if (_authApplication != null) {
                storeClientId(_authApplication!!.clientId)
            }

        }
    }


    suspend fun storeClientId(clientId: String) {
        settingsDataStore.edit { preferences ->
            preferences[stringPreferencesKey("client_id")] = clientId
        }
    }

    fun getClientIdFromStorage(): Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[stringPreferencesKey("client_id")] ?: ""
    }

}