package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.prefs

import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import me.tatarka.inject.annotations.Inject

@Inject
class ClearCacheViewModel(
    private val platform: Platform
): ViewModel() {
    fun getCacheSizeInBytes() = platform.getCacheSizeInBytes()
    fun cleanCache() {
        platform.cleanCache()
    }
}