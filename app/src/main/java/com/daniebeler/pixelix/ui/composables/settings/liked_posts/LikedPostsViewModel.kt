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
) : ViewModel() {

    var likedPostsState by mutableStateOf(LikedPostsState())

    init {
        getItemsFirstLoad()
    }

    fun getItemsFirstLoad(refreshing: Boolean = false) {
        repository.getLikedPosts().onEach { result ->
            likedPostsState = when (result) {
                is Resource.Success -> {
                    LikedPostsState(
                        likedPosts = result.data?.posts ?: emptyList(),
                        nextMaxId = result.data?.nextId ?: ""
                    )
                }

                is Resource.Error -> {
                    LikedPostsState(
                        likedPosts = likedPostsState.likedPosts,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    LikedPostsState(
                        likedPosts = likedPostsState.likedPosts,
                        isLoading = true,
                        isRefreshing = refreshing
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getItemsPaginated() {
        if (likedPostsState.likedPosts.isNotEmpty() && !likedPostsState.isLoading && likedPostsState.nextMaxId.isNotEmpty()) {
            println("swarox")
            repository.getLikedPosts(likedPostsState.nextMaxId).onEach { result ->
                likedPostsState = when (result) {
                    is Resource.Success -> {
                        LikedPostsState(
                            likedPosts = likedPostsState.likedPosts + (result.data?.posts
                                ?: emptyList()),
                            nextMaxId = result.data?.nextId ?: "",
                            error = "",
                            isLoading = false,
                            isRefreshing = false
                        )
                    }

                    is Resource.Error -> {
                        LikedPostsState(
                            likedPosts = likedPostsState.likedPosts,
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false,
                            isRefreshing = false
                        )
                    }

                    is Resource.Loading -> {
                        LikedPostsState(
                            likedPosts = likedPostsState.likedPosts,
                            error = "",
                            isLoading = true,
                            isRefreshing = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun refresh() {
        getItemsFirstLoad(true)
    }

}