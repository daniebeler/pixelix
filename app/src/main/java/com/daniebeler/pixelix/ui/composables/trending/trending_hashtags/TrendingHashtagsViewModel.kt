package com.daniebeler.pixelix.ui.composables.trending.trending_hashtags

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
class TrendingHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var trendingHashtagsState by mutableStateOf(TrendingHashtagsState())

    init {
        getTrendingHashtags()
    }

    private fun getTrendingHashtags() {
        repository.getTrendingHashtags().onEach { result ->
            trendingHashtagsState = when (result) {
                is Resource.Success -> {
                    TrendingHashtagsState(trendingHashtags = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    TrendingHashtagsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    TrendingHashtagsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}