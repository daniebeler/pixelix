package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.content.pm.PackageManager
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
actual class EnableCustomAppIconUseCase {
    actual operator fun invoke(context: KmpContext, name: String) {
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