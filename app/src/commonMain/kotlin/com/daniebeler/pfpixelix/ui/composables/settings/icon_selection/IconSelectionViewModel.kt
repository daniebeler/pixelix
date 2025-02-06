package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.disableCustomIcon
import com.daniebeler.pfpixelix.utils.enableCustomIcon
import com.daniebeler.pfpixelix.utils.getAppIcons
import me.tatarka.inject.annotations.Inject

@Inject
class IconSelectionViewModel : ViewModel() {

    val icons = mutableStateListOf<IconWithName>()

    fun updateList(context: KmpContext) {
        icons.clear()
        icons.addAll(context.getAppIcons())
    }

    fun changeIcon(context: KmpContext, name: String) {
        context.disableCustomIcon()
        context.enableCustomIcon(icons.first { it.name == name })
        updateList(context)
    }
}

data class IconWithName(
    val name: String, val icon: ImageBitmap, val enabled: Boolean = false
)