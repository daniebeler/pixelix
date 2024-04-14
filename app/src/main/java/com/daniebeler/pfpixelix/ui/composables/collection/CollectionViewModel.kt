package com.daniebeler.pfpixelix.ui.composables.collection

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.common.Resource
import com.daniebeler.pfpixelix.domain.usecase.GetCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.GetPostsOfCollectionUseCase
import com.daniebeler.pfpixelix.domain.usecase.OpenExternalUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionUseCase: GetCollectionUseCase,
    private val getPostsOfCollectionUseCase: GetPostsOfCollectionUseCase,
    private val openExternalUrlUseCase: OpenExternalUrlUseCase
) : ViewModel() {

    var collectionState by mutableStateOf(CollectionState())
    var collectionPostsState by mutableStateOf(CollectionPostsState())

    fun loadData(collectionId: String) {
        if (collectionState.id == null) {
            collectionState = collectionState.copy(id = collectionId)
            getCollection()
            getPostsFirstLoad(false)
        }
    }

    private fun getCollection() {
        if (collectionState.id != null) {
            getCollectionUseCase(collectionState.id!!).onEach { result ->
                collectionState = when (result) {
                    is Resource.Success -> {
                        CollectionState(
                            collection = result.data, id = collectionState.id
                        )
                    }

                    is Resource.Error -> {
                        CollectionState(
                            error = result.message ?: "An unexpected error occurred",
                            id = collectionState.id
                        )
                    }

                    is Resource.Loading -> {
                        CollectionState(
                            isLoading = true,
                            collection = collectionState.collection,
                            id = collectionState.id
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    private fun getPostsFirstLoad(refreshing: Boolean) {
        if (collectionState.id != null) {
            getPostsOfCollectionUseCase(collectionState.id!!).onEach { result ->
                collectionPostsState = when (result) {
                    is Resource.Success -> {
                        val endReached = (result.data?.size ?: 0) == 0
                        CollectionPostsState(
                            posts = result.data ?: emptyList(), endReached = endReached
                        )
                    }

                    is Resource.Error -> {
                        CollectionPostsState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }

                    is Resource.Loading -> {
                        CollectionPostsState(
                            isLoading = true,
                            isRefreshing = refreshing,
                            posts = collectionPostsState.posts
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }

    }


    fun refresh() {
        getPostsFirstLoad(true)
    }

    fun openUrl(context: Context, url: String) {
        openExternalUrlUseCase(context, url)
    }
}