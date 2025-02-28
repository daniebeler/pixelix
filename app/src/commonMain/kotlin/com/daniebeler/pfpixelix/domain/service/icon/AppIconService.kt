package com.daniebeler.pfpixelix.domain.service.icon

import com.daniebeler.pfpixelix.domain.service.platform.Platform
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*

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

    fun getCurrentIcon() = iconManager.getCurrentIcon()

    fun selectIcon(icon: DrawableResource) {
        iconManager.setCustomIcon(icon)
    }
}