package com.daniebeler.pixels.ui.composables.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedPostsViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var bookmarkedPosts: List<Post> by mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            bookmarkedPosts = repository.getBookmarkedPosts()
        }
    }

}