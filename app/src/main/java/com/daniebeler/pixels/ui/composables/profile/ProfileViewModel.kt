package com.daniebeler.pixels.ui.composables.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.ui.composables.trending.trending_accounts.TrendingAccountsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var relationshipState by mutableStateOf(RelationshipState())
    var mutualFollowersState by mutableStateOf(MutualFollowersState())
    var posts: List<Post> by mutableStateOf(emptyList())


    fun loadData(userId: String) {
        getAccount(userId)

        CoroutineScope(Dispatchers.Default).launch {
            posts = repository.getPostsByAccountId(userId)
        }

        getRelationship(userId)

        getMututalFollowers(userId)
    }

    private fun getRelationship(userId: String) {
        repository.getRelationships(List(1) {userId}).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data!![0])
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMututalFollowers(userId: String) {
        repository.getMutalFollowers(userId).onEach { result ->
             mutualFollowersState = when (result) {
                is Resource.Success -> {
                    MutualFollowersState(mutualFollowers = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    MutualFollowersState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    MutualFollowersState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getAccount(userId: String) {
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

    fun loadMorePosts(userId: String) {
        if (posts.isNotEmpty()) {
            var maxId = posts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                posts = posts + repository.getPostsByAccountId(userId, maxId)
            }
        }
    }

    fun followAccount(userId: String) {
        repository.followAccount(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun unfollowAccount(userId: String) {
        repository.unfollowAccount(userId).onEach { result ->
            relationshipState = when (result) {
                is Resource.Success -> {
                    RelationshipState(accountRelationship = result.data)
                }

                is Resource.Error -> {
                    RelationshipState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    RelationshipState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun muteAccount() {
        /*if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.muteAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }*/
    }

    fun unmuteAccount() {
        /*if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unmuteAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }*/
    }

    fun blockAccount() {
        /*if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.blockAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }*/
    }

    fun unblockAccount() {
        /*if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unblockAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }*/
    }
}