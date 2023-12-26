package com.daniebeler.pixels.ui.composables.timelines

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalTimelineViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var localTimeline: List<Post> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            localTimeline = repository.getLocalTimeline()
        }
    }
}