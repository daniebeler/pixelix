package com.daniebeler.pfpixelix.domain.service.icon

import com.daniebeler.pfpixelix.di.AppSingleton
import com.daniebeler.pfpixelix.domain.service.platform.Platform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*

@AppSingleton
@Inject
class AppIconService(
    platform: Platform
) {
    private val iconManager = platform.getAppIconManager()

    val icons = listOf(
        Res.drawable.app_icon_00,
        Res.drawable.app_icon_01,
        Res.drawable.app_icon_02,
        Res.drawable.app_icon_03,
        Res.drawable.app_icon_05,
        Res.drawable.app_icon_06,
        Res.drawable.app_icon_07,
        Res.drawable.app_icon_08,
        Res.drawable.app_icon_09,
    )

    private val currentIconFlow = MutableStateFlow(iconManager.getCurrentIcon())
    val currentIcon: StateFlow<DrawableResource> = currentIconFlow

    fun selectIcon(icon: DrawableResource) {
        iconManager.setCustomIcon(icon)
        currentIconFlow.value = icon
    }
}