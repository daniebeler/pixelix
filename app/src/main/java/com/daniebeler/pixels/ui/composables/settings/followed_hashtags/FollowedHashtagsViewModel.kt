package com.daniebeler.pixels.ui.composables.settings.followed_hashtags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.Tag
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.ui.composables.settings.liked_posts.LikedPostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedHashtagsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var followedHashtagsState by mutableStateOf(FollowedHashtagsState())

    init {
        getFollowedHashtags()
    }

    private fun getFollowedHashtags() {
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