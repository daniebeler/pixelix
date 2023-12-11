package com.daniebeler.pixels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.models.api.Post
import com.daniebeler.pixels.models.api.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    private var _trendingPosts by mutableStateOf(emptyList<Post>())
    private var _localTimeline by mutableStateOf(emptyList<Post>())

    val trendingPosts: List<Post>
        get() = _trendingPosts

    val localTimeline: List<Post>
        get() = _localTimeline

    fun getTrendingPosts() {
        viewModelScope.launch {
            _trendingPosts = repository.getTrendingPosts()
        }
    }

    fun getLocalTimeline() {
        viewModelScope.launch {
            _localTimeline = repository.getLocalTimeline()
        }
    }
}