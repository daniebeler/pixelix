package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetDomainSoftwareUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountIdUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnAccountUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnInstanceDomainUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetOwnPostsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetViewUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import com.daniebeler.pfpixelix.domain.usecase.SetViewUseCase
import com.daniebeler.pfpixelix.ui.composables.profile.AccountState
import com.daniebeler.pfpixelix.ui.composables.profile.CollectionsState
import com.daniebeler.pfpixelix.ui.composables.profile.DomainSoftwareState
import com.daniebeler.pfpixelix.ui.composables.profile.PostsState
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val getOwnAccountUseCase: GetOwnAccountUseCase,
    private val getOwnPostsUseCase: GetOwnPostsUseCase,
    private val getOwnInstanceDomainUseCase: GetOwnInstanceDomainUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase,
    private val getDomainSoftwareUseCase: GetDomainSoftwareUseCase,
    private val getViewUseCase: GetViewUseCase,
    private val setViewUseCase: SetViewUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getOwnAccountIdUseCase: GetOwnAccountIdUseCase,
    application: android.app.Application
) : AndroidViewModel(application) {

    var accountState by mutableStateOf(AccountState())
    var postsState by mutableStateOf(PostsState())
    var ownDomain by mutableStateOf("")
    var domainSoftwareState by mutableStateOf(DomainSoftwareState())
    var context = application
    var view by mutableStateOf(ViewEnum.Loading)

    var collectionsState by mutableStateOf(CollectionsState())

    init {
        loadData()

        viewModelScope.launch {
            getViewUseCase().collect { res ->
                view = res
            }
        }

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

        viewModelScope.launch {
            getCollections(getOwnAccountIdUseCase().first())

        }
    }

    private fun getAccount() {
        getOwnAccountUseCase().onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    val domain = result.data?.url?.substringAfter("https://")
                        ?.substringBefore("/") ?: ""
                    getDomainSoftware(domain)
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

    private fun getCollections(userId: String) {
        getCollectionsUseCase(userId).onEach { result ->
            collectionsState = when (result) {
                is Resource.Success -> {
                    CollectionsState(collections = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    CollectionsState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    CollectionsState(
                        isLoading = true, collections = collectionsState.collections
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getDomainSoftware(domain: String) {
        getDomainSoftwareUseCase(domain, context).onEach { result ->
            domainSoftwareState = when (result) {
                is Resource.Success -> {
                    DomainSoftwareState(domainSoftware = result.data)
                }

                is Resource.Error -> {
                    DomainSoftwareState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    DomainSoftwareState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(context, url)
    }

    fun changeView(newView: ViewEnum) {
        view = newView
        viewModelScope.launch {
            setViewUseCase(newView)
        }
    }

    fun postGetsDeleted(postId: String) {
        postsState = postsState.copy(posts = postsState.posts.filter { post -> post.id != postId })
    }
}