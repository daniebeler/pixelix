package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.util.DisplayMetrics
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.daniebeler.pfpixelix.common.IconsHolder
import com.daniebeler.pfpixelix.utils.KmpContext
import me.tatarka.inject.annotations.Inject

@Inject
actual class GetActiveAppIconUseCase {
    actual operator fun invoke(context: KmpContext): ImageBitmap? {
        try {
            val packageManager = context.packageManager

            val list = IconsHolder.list

            list.forEach {
                val isEnabled = packageManager.getComponentEnabledSetting(
                    ComponentName(
                        context, it.name
                    )
                )

                if (isEnabled == 1) {
                    val launcherDrawable02 = ResourcesCompat.getDrawableForDensity(
                        context.resources,
                        it.iconResourceId,
                        DisplayMetrics.DENSITY_XXXHIGH,
                        context.theme
                    )

                    val bm02 = launcherDrawable02!!.toBitmap(
                        launcherDrawable02.minimumWidth, launcherDrawable02.minimumHeight
                    ).asImageBitmap()

                    return bm02
                }
            }
        } catch (e: Error) {
            return null
        }

        return null
    }
}