package com.daniebeler.pixelix.ui.composables.settings.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.daniebeler.pixelix.domain.usecase.GetHideSensitiveContent
import com.daniebeler.pixelix.domain.usecase.StoreHideSensitiveContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val storeHideSensitiveContentUseCase: StoreHideSensitiveContent,
    private val getHideSensitiveContent: GetHideSensitiveContent
) : ViewModel() {
    var isSensitiveContentHidden by mutableStateOf(true)

    init {
        getHideSensitiveContent().asLiveData()
    }

    fun storeHideSensitiveContent(value: Boolean) {
        isSensitiveContentHidden = value
        viewModelScope.launch {
            storeHideSensitiveContentUseCase(value)
        }
    }
}