package com.daniebeler.pixelix.ui.composables.trending.trending_posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingPostsViewModel @Inject constructor(
    private val repository: CountryRepository
) : ViewModel() {

    var trendingState by mutableStateOf(TrendingPostsState())
        private set

    fun getTrendingPosts(timeRange: String, refreshing: Boolean = false) {
        repository.getTrendingPosts(timeRange).onEach { result ->
            trendingState = when (result) {
                is Resource.Success -> {
                    TrendingPostsState(trendingPosts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    TrendingPostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    TrendingPostsState(
                        isLoading = true,
                        isRefreshing = refreshing,
                        trendingPosts = trendingState.trendingPosts
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}