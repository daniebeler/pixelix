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

    var account: Account? by mutableStateOf(null)
    var relationshipState by mutableStateOf(RelationshipState())
    var mutalFollowers: List<Account> by mutableStateOf(emptyList())
    var posts: List<Post> by mutableStateOf(emptyList())




    fun loadData(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            account = repository.getAccount(userId)
        }

        CoroutineScope(Dispatchers.Default).launch {
            posts = repository.getPostsByAccountId(userId)
        }

        getRelationship(userId)

        CoroutineScope(Dispatchers.Default).launch {
            mutalFollowers = repository.getMutalFollowers(userId)
        }
    }

    private fun getRelationship(userId: String) {
        repository.getRelationships(List<String>(1) {userId}).onEach { result ->
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

    fun loadMorePosts(userId: String) {
        if (posts.isNotEmpty()) {
            var maxId = posts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                posts = posts + repository.getPostsByAccountId(userId, maxId)
            }
        }
    }

    fun followAccount(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.followAccount(userId)
            if (res != null) {
                getRelationship(userId)
            }
        }
    }

    fun unfollowAccount(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.unfollowAccount(userId)
            if (res != null) {
                getRelationship(userId)
            }
        }
    }

    fun muteAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.muteAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }
    }

    fun unmuteAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unmuteAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }
    }

    fun blockAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.blockAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }
    }

    fun unblockAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unblockAccount(account!!.id)
                if (res != null) {
                    getRelationship(account!!.id)
                }
            }
        }
    }
}