package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import me.tatarka.inject.annotations.Inject

@Inject
class IconSelectionViewModel(
    val platform: Platform
) : ViewModel() {
    private val iconManager = platform.getAppIconManager()

    val icons = mutableStateListOf<IconWithName>()

    fun updateList() {
        icons.clear()
        icons.addAll(iconManager.getIcons())
    }

    fun changeIcon(name: String) {
        iconManager.disableCustomIcon()
        iconManager.enableCustomIcon(icons.first { it.name == name })
        updateList()
    }
}

data class IconWithName(
    val name: String, val icon: ImageBitmap, val enabled: Boolean = false
)