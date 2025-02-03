package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import me.tatarka.inject.annotations.Inject

@Inject
class EnableCustomAppIconUseCase {

    operator fun invoke(context: Context, name: String) {
        try {
            val packageManager = context.packageManager
            val componentName = ComponentName(context, name)
            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (_: Error) {

        }
    }
}