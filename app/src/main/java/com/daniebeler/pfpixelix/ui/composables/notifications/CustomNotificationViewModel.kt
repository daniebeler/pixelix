package com.daniebeler.pfpixelix.ui.composables.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CustomNotificationViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
): ViewModel() {
    var ancestor by mutableStateOf<Post?>(null)

    fun loadAncestor(postId: String) {
        getPostUseCase(postId).onEach { result ->
            if (result is Resource.Success) {
                ancestor = result.data!!
            }
        }.launchIn(viewModelScope)
    }
}