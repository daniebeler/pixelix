package com.daniebeler.pixelix.ui.composables.settings.liked_posts

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
class LikedPostsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var likedPostsState by mutableStateOf(LikedPostsState())

    init {
        getLikedPosts()
    }

    private fun getLikedPosts() {
        repository.getLikedPosts().onEach { result ->
            likedPostsState = when (result) {
                is Resource.Success -> {
                    LikedPostsState(likedPosts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    LikedPostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    LikedPostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}