package com.daniebeler.pixels.ui.composables.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.domain.model.Tag
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var followedHashtags: List<Tag> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            followedHashtags = repository.getFollowedHashtags()
        }
    }

}