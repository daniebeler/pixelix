package com.daniebeler.pixels.ui.composables.followers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.ui.composables.profile.AccountState
import com.daniebeler.pixels.ui.composables.trending.trending_posts.TrendingPostsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var followersState by mutableStateOf(FollowersState())
    var followingState by mutableStateOf(FollowingState())

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

    fun getFollowers(accountId: String) {
        repository.getAccountsFollowers(accountId).onEach { result ->
            followersState = when (result) {
                is Resource.Success -> {
                    FollowersState(followers = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    FollowersState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowersState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFollowing(accountId: String) {
        repository.getAccountsFollowing(accountId).onEach { result ->
            followingState = when (result) {
                is Resource.Success -> {
                    FollowingState(following = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    FollowingState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    FollowingState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}