package com.daniebeler.pfpixelix.ui.composables.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetNotificationsUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionUseCase: GetCollectionUseCase,
    private val getPostsOfCollectionUseCase: GetPostsOfCollectionUseCase
) : ViewModel() {

    var collectionState by mutableStateOf(CollectionState())

    fun loadData(collectionId: String) {
        if (collectionState.id == null) {
            collectionState = collectionState.copy(id = collectionId)
            getCollectionsFirstLoad(false)
        }
    }

    private fun getCollectionsFirstLoad(refreshing: Boolean) {
        if (collectionState.id != null) {
            getPostsOfCollectionUseCase(collectionState.id!!).onEach { result ->
                collectionState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) == 0
                        CollectionState(posts = result.data ?: emptyList(), endReached = endReached, collection = collectionState.collection)
                    }

                    is Resource.Error -> {
                        CollectionState(error = result.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        CollectionState(
                            isLoading = true,
                            isRefreshing = refreshing,
                            posts = collectionState.posts
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }

    }


    fun refresh() {
        getCollectionsFirstLoad(true)
    }
}