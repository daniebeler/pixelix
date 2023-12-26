package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingPostsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var dailyTrendingPosts: List<Post> by mutableStateOf(emptyList())
    var monthlyTrendingPosts: List<Post> by mutableStateOf(emptyList())
    var yearlyTrendingPosts: List<Post> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            dailyTrendingPosts = repository.getTrendingPosts("daily")
        }

        viewModelScope.launch {
            monthlyTrendingPosts = repository.getTrendingPosts("monthly")
        }

        viewModelScope.launch {
            yearlyTrendingPosts = repository.getTrendingPosts("yearly")
        }
    }
}