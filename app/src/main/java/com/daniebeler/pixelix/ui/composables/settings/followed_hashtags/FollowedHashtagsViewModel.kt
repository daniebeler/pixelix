package com.daniebeler.pixelix.ui.composables.settings.followed_hashtags

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
class FollowedHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var followedHashtagsState by mutableStateOf(FollowedHashtagsState())

    init {
        getFollowedHashtags()
    }

    fun getFollowedHashtags() {
        repository.getFollowedHashtags().onEach { result ->
            followedHashtagsState = when (result) {
                is Resource.Success -> {
                    FollowedHashtagsState(followedHashtags = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    FollowedHashtagsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowedHashtagsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}