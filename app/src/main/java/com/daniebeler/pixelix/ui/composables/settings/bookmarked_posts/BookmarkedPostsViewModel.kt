package com.daniebeler.pixelix.ui.composables.settings.bookmarked_posts

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
class BookmarkedPostsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var bookmarkedPostsState by mutableStateOf(BookmarkedPostsState())

    init {
        getBookmarkedPosts()
    }

    fun getBookmarkedPosts() {
        repository.getBookmarkedPosts().onEach { result ->
            bookmarkedPostsState = when (result) {
                is Resource.Success -> {
                    BookmarkedPostsState(bookmarkedPosts = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    BookmarkedPostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    BookmarkedPostsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}