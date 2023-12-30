package com.daniebeler.pixels.ui.composables.trending.trending_posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingPostsViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var dailyState by mutableStateOf(TrendingPostsState())
        private set
    //var monthlyTrendingPosts: Flow<Resource<List<Post>>> by mutableStateOf(emptyList())
    //var yearlyTrendingPosts: Flow<Resource<List<Post>>> by mutableStateOf(emptyList())

    init {
        getTrendingPosts("daily")
    }

    private fun getTrendingPosts(timeRange: String) {
        repository.getTrendingPosts(timeRange).onEach { result ->
            dailyState = when (result) {
                is Resource.Success -> {
                    TrendingPostsState(trendingPosts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    TrendingPostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    TrendingPostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}