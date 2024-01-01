package com.daniebeler.pixels.ui.composables.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixels.common.Resource
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.ui.composables.profile.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var accountState by mutableStateOf(AccountState())
    var ownPosts: List<Post> by mutableStateOf(emptyList())
    private var accountId: String = ""

    init {
        CoroutineScope(Dispatchers.Default).launch {
            accountId = repository.getAccountId().first()
            CoroutineScope(Dispatchers.Default).launch {
                ownPosts = repository.getPostsByAccountId(accountId)
            }

            getAccount(accountId)
        }
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

    fun loadMorePosts() {
        if (ownPosts.isNotEmpty()) {
            val maxId = ownPosts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                ownPosts += repository.getPostsByAccountId(accountId, maxId)
            }
        }
    }
}