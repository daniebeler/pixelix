package com.daniebeler.pfpixelix.domain.usecase

import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.IconsHolder

class GetActiveAppIconUseCase() {

    operator fun invoke(context: Context): ImageBitmap {
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

        val drawable: Drawable = R.drawable.pixelix_logo.toDrawable()

        return drawable.toBitmap(
            drawable.minimumWidth, drawable.minimumHeight
        ).asImageBitmap()
    }
}