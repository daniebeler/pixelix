package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.daniebeler.pfpixelix.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IconSelectionViewModel @Inject constructor(

) : ViewModel() {

    var icons = mutableListOf<IconWithName>()

    fun fillList(context: Context) {
        if (icons.isEmpty()) {
            val packageManager = context.packageManager


            val list = listOf(
                IconAndName("com.daniebeler.pfpixelix.MainActivity", R.mipmap.ic_launcher_02),
                IconAndName("com.daniebeler.pfpixelix.Icon03", R.mipmap.ic_launcher_03),
                IconAndName("com.daniebeler.pfpixelix.Icon01", R.mipmap.ic_launcher_01),
                IconAndName("com.daniebeler.pfpixelix.Icon05", R.mipmap.ic_launcher_05),
                IconAndName("com.daniebeler.pfpixelix.Icon06", R.mipmap.ic_launcher_06),
                IconAndName("com.daniebeler.pfpixelix.Icon07", R.mipmap.ic_launcher_07),
                IconAndName("com.daniebeler.pfpixelix.Icon04", R.mipmap.ic_launcher)
            )

            list.forEach {
                val launcherDrawable02 = ResourcesCompat.getDrawableForDensity(
                    context.resources,
                    it.iconResourceId,
                    DisplayMetrics.DENSITY_XXXHIGH,
                    context.theme
                )

                var bm02 = launcherDrawable02!!.toBitmap(
                    launcherDrawable02.minimumWidth, launcherDrawable02.minimumHeight
                ).asImageBitmap()

                val bm02Enabled = packageManager.getComponentEnabledSetting(
                    ComponentName(
                        context, it.name
                    )
                )

                icons.add(IconWithName(it.name, bm02, bm02Enabled == 1))
            }
        }
    }

    fun changeIcon(context: Context, name: String) {
        try {
            // Get the package manager instance
            val packageManager = context.packageManager

            val mainActivityComponent = ComponentName(context, name)
            println("ffffief: " + packageManager.getComponentEnabledSetting(mainActivityComponent))
            // Enable the main activity component
            packageManager.setComponentEnabledSetting(
                mainActivityComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            icons.forEach {
                if (it.name != name) {
                    val newYearComponent = ComponentName(context, it.name)
                    packageManager.setComponentEnabledSetting(
                        newYearComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }

            Toast.makeText(context, "Changed Logo", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Log.e("Exceptionn", e.toString())
        }
    }

    data class IconAndName(
        val name: String, @DrawableRes val iconResourceId: Int
    )
}

data class IconWithName(
    val name: String, val icon: ImageBitmap, val enabled: Boolean = false
)