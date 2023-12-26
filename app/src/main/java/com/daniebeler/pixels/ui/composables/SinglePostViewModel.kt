package com.daniebeler.pixels.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniebeler.pixels.domain.model.Post
import com.daniebeler.pixels.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinglePostViewModel @Inject constructor(
    private val repository: CountryRepository
): ViewModel() {

    var post: Post? by mutableStateOf(null)

    fun loadPost(postId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            post = repository.getPostById(postId)
        }
    }
}