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

    private var _dailyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _monthlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _yearlyTrendingPosts by mutableStateOf(emptyList<Post>())
    private var _localTimeline by mutableStateOf(emptyList<Post>())

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
}