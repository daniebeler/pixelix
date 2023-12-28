package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.model.Relationship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var account: Account? by mutableStateOf(null)
    var relationship: Relationship? by mutableStateOf(null)
    var mutalFollowers: List<Account> by mutableStateOf(emptyList())
    var posts: List<Post> by mutableStateOf(emptyList())


    fun loadData(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            account = repository.getAccount(userId)
        }

        CoroutineScope(Dispatchers.Default).launch {
            posts = repository.getPostsByAccountId(userId)
        }

        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.getRelationships(userId)
            if (res != null) {
                if (res.isNotEmpty()) {
                    relationship = res[0]
                }
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            mutalFollowers = repository.getMutalFollowers(userId)
        }
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
                relationship = res
            }
        }
    }

    fun unfollowAccount(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            var res = repository.unfollowAccount(userId)
            if (res != null) {
                relationship = res
            }
        }
    }

    fun muteAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.muteAccount(account!!.id)
                if (res != null) {
                    relationship = res
                }
            }
        }
    }

    fun unmuteAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unmuteAccount(account!!.id)
                if (res != null) {
                    relationship = res
                }
            }
        }
    }

    fun blockAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.blockAccount(account!!.id)
                if (res != null) {
                    relationship = res
                }
            }
        }
    }

    fun unblockAccount() {
        if (account != null) {
            CoroutineScope(Dispatchers.Default).launch {
                var res = repository.unblockAccount(account!!.id)
                if (res != null) {
                    relationship = res
                }
            }
        }
    }
}