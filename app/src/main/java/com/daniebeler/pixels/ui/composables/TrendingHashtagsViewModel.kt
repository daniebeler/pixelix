package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.api.CountryRepository
import com.daniebeler.pixels.domain.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var trendingHashtags: List<Tag> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            trendingHashtags = repository.getTrendingHashtags()
        }
    }
}