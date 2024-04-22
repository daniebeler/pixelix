package com.daniebeler.pfpixelix.ui.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.usecase.GetThemeUseCase
import com.daniebeler.pfpixelix.domain.usecase.StoreThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val storeThemeUseCase: StoreThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase
): ViewModel() {
    var currentTheme by mutableStateOf(ThemeState(isLoading = true))

    init {
        viewModelScope.launch {
            getThemeUseCase().collect { res ->
                currentTheme = ThemeState(theme = res)
            }
        }
    }

    fun storeTheme(value: String) {
        currentTheme = ThemeState(theme = value)
        viewModelScope.launch {
            storeThemeUseCase(value)
        }
    }
}