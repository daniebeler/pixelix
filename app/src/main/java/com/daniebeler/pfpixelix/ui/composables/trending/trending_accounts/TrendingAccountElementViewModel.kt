package com.daniebeler.pfpixelix.ui.composables.trending.trending_accounts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingAccountElementViewModel @Inject constructor(
    private val getAccountPosts: GetPostsOfAccountUseCase,
) : ViewModel() {
    var postsState by mutableStateOf(TrendingAccountPostsState())

    fun loadItems(accountId: String) {
        if (postsState.posts.isEmpty()) {
            getAccountPosts(accountId).onEach { result ->
                postsState = when (result) {
                    is Resource.Success -> {
                        TrendingAccountPostsState(
                            posts = result.data ?: emptyList(), error = "", isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        TrendingAccountPostsState(
                            posts = postsState.posts,
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        TrendingAccountPostsState(
                            posts = postsState.posts, error = "", isLoading = true
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}