package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import android.content.ComponentName
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.IconsHolder
import com.daniebeler.pfpixelix.domain.usecase.DisableAllCustomAppIconsUseCase
import com.daniebeler.pfpixelix.domain.usecase.EnableCustomAppIconUseCase
import me.tatarka.inject.annotations.Inject

class IconSelectionViewModel @Inject constructor(
    private val disableAllCustomAppIconsUseCase: DisableAllCustomAppIconsUseCase,
    private val enableCustomAppIconUseCase: EnableCustomAppIconUseCase
) : ViewModel() {

    var icons = mutableListOf<IconWithName>()

    fun fillList(context: Context) {
        if (icons.isEmpty()) {
            val mainDrawable = ResourcesCompat.getDrawableForDensity(
                context.resources,
                R.mipmap.ic_launcher_02,
                DisplayMetrics.DENSITY_XXXHIGH,
                context.theme
            )

            val mainBitmap = mainDrawable!!.toBitmap(
                mainDrawable.minimumWidth, mainDrawable.minimumHeight
            ).asImageBitmap()
            icons.add(IconWithName("com.daniebeler.pfpixelix.MainActivity", mainBitmap, false))

            IconsHolder.list.forEach {
                val drawable = ResourcesCompat.getDrawableForDensity(
                    context.resources,
                    it.iconResourceId,
                    DisplayMetrics.DENSITY_XXXHIGH,
                    context.theme
                )

                val bitmap = drawable!!.toBitmap(
                    drawable.minimumWidth, drawable.minimumHeight
                ).asImageBitmap()

                icons.add(IconWithName(it.name, bitmap, false))
            }

            setEnabledValues(context)
        }
    }

    private fun setEnabledValues(context: Context) {
        val newList = mutableListOf<IconWithName>()
        val packageManager = context.packageManager

        var foundItem = false

        icons.forEach {
            if (it.name == "com.daniebeler.pfpixelix.MainActivity") {
                newList.add(IconWithName(it.name, it.icon, false))
            }
            else {
                val enabled = packageManager.getComponentEnabledSetting(
                    ComponentName(
                        context, it.name
                    )
                )

                foundItem = foundItem || enabled == 1

                newList.add(IconWithName(it.name, it.icon, enabled == 1))
            }
        }

        if (!foundItem) {
            if (newList.size > 0) {
                newList[0] = IconWithName(icons[0].name, icons[0].icon, true)
            }
        }

        icons = newList
    }

    fun changeIcon(context: Context, name: String) {
        disableAllCustomAppIconsUseCase(context)
        enableCustomAppIconUseCase(context, name)
        setEnabledValues(context)
    }

    data class IconAndName(
        val name: String, @DrawableRes val iconResourceId: Int
    )
}

data class IconWithName(
    val name: String, val icon: ImageBitmap, val enabled: Boolean = false
)