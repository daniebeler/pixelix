package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.domain.service.icon.AppIconService
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource

@Inject
class IconSelectionViewModel(
    val appIconService: AppIconService
) : ViewModel() {
    val icons = appIconService.icons
    val selectedIcon = mutableStateOf(appIconService.getCurrentIcon())

    fun changeIcon(icon: DrawableResource) {
        selectedIcon.value = icon
        appIconService.selectIcon(icon)
    }
}
