package com.daniebeler.pixelix.ui.composables.followers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.profile.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var followersState by mutableStateOf(FollowersState())
    var followingState by mutableStateOf(FollowingState())

    var accountId: String = ""

    fun getAccount(userId: String) {
        repository.getAccount(userId).onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    AccountState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setAccountIdValue(id: String) {
        accountId = id
    }

    fun getFollowersFirstLoad(refreshing: Boolean = false) {
        repository.getAccountsFollowers(accountId).onEach { result ->
            followersState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) < 40
                    FollowersState(followers = result.data ?: emptyList(), endReached = endReached)
                }

                is Resource.Error -> {
                    FollowersState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowersState(isLoading = true, isRefreshing = refreshing, followers = followersState.followers)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFollowersPaginated() {
        if (followersState.followers.isNotEmpty() && !followersState.isLoading && !followersState.endReached) {
            repository.getAccountsFollowers(accountId, followersState.followers.last().id).onEach { result ->
                followersState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) < 40
                        FollowersState(
                            followers = followersState.followers + (result.data ?: emptyList()),
                            endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        FollowersState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        FollowersState(isLoading = true, isRefreshing = false, followers = followersState.followers)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getFollowingFirstLoad(refreshing: Boolean = false) {
        repository.getAccountsFollowing(accountId).onEach { result ->
            followingState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data?.size ?: 0) < 40
                    FollowingState(following = result.data ?: emptyList(), endReached = endReached)
                }

                is Resource.Error -> {
                    FollowingState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowingState(isLoading = true, isRefreshing = refreshing, following = followingState.following)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFollowingPaginated() {
        if (followingState.following.isNotEmpty() && !followingState.isLoading && !followingState.endReached) {
            repository.getAccountsFollowing(accountId, followingState.following.last().id).onEach { result ->
                followingState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) < 40
                        FollowingState(
                            following = followingState.following + (result.data ?: emptyList()),
                            endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        FollowingState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        FollowingState(isLoading = true, isRefreshing = false, following = followingState.following)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}