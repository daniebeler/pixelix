package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnPostsUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val getOwnAccountUseCase: GetOwnAccountUseCase,
    private val getOwnPostsUseCase: GetOwnPostsUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase
) : ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var postsState by mutableStateOf(PostsState())
    var ownDomain by mutableStateOf("")

    init {
        loadData()

        viewModelScope.launch {
            getInstanceDomain()
        }

    }

    private suspend fun getInstanceDomain() {
        getOwnInstanceDomainUseCase().collect { res ->
            ownDomain = res
        }
    }

    fun loadData() {
        getAccount()
        getPostsFirstLoad()
    }

    private fun getAccount() {
        getOwnAccountUseCase().onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true, account = accountState.account)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPostsFirstLoad() {
        getOwnPostsUseCase().onEach { result ->
            postsState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) < Constants.PROFILE_POSTS_LIMIT
                    PostsState(posts = result.data ?: emptyList(), endReached = endReached)
                }

                is Resource.Error -> {
                    PostsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    PostsState(isLoading = true, posts = postsState.posts)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPostsPaginated() {
        if (postsState.posts.isNotEmpty() && !postsState.isLoading && !postsState.endReached) {
            getOwnPostsUseCase(postsState.posts.last().id).onEach { result ->
                postsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) < Constants.PROFILE_POSTS_LIMIT
                        PostsState(
                            posts = postsState.posts + (result.data ?: emptyList()),
                            endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        PostsState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        PostsState(isLoading = true, posts = postsState.posts)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(context, url)
    }
}