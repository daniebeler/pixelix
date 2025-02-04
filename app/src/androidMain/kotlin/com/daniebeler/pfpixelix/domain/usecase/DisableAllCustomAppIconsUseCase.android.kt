package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.content.pm.PackageManager
import com.daniebeler.pfpixelix.common.IconsHolder
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
actual class DisableAllCustomAppIconsUseCase actual constructor() {
    actual operator fun invoke(context: KmpContext) {
        try {
            val packageManager = context.packageManager
            IconsHolder.list.forEach {
                val componentName = ComponentName(context, it.name)
                packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        } catch (_: Error) {

        }
    }
}