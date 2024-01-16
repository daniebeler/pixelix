package com.daniebeler.pixelix.ui.composables.settings.about_instance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.common.Resource
import com.daniebeler.pixelix.domain.repository.CountryRepository
import com.daniebeler.pixelix.ui.composables.profile.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AboutInstanceViewModel @Inject constructor(
    val repository: CountryRepository
) : ViewModel() {

    var instanceState by mutableStateOf(InstanceState())

    init {
        getInstance()
    }

    private fun getInstance() {
        repository.getInstance().onEach { result ->
            println(result)
            instanceState = when (result) {
                is Resource.Success -> {
                    InstanceState(instance = result.data)
                }

                is Resource.Error -> {
                    InstanceState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    InstanceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}