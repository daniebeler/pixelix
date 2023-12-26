package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.repository.CountryRepository
import com.daniebeler.pixels.domain.model.Account
import com.daniebeler.pixels.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var ownAccount: Account? by mutableStateOf(null)
    var ownPosts: List<Post> by mutableStateOf(emptyList())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            ownPosts = repository.getPostsByAccountId("497910174831013185")
        }

        CoroutineScope(Dispatchers.Default).launch {
            ownAccount = repository.getAccount("497910174831013185")
        }
    }

    fun loadMorePosts() {
        if (ownPosts.isNotEmpty()) {
            val maxId = ownPosts.last().id

            CoroutineScope(Dispatchers.Default).launch {
                ownPosts += repository.getPostsByAccountId("497910174831013185", maxId)
            }
        }
    }
}