package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.daniebeler.pfpixelix.common.IconsHolder
import me.tatarka.inject.annotations.Inject

@Inject
class DisableAllCustomAppIconsUseCase() {

    operator fun invoke(context: Context) {
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