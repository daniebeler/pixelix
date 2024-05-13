package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var icons by mutableStateOf<List<IconWithName>>(emptyList())

    fun fillList(context: Context) {

        val packageManager = context.packageManager

        val launcherDrawable = ResourcesCompat.getDrawableForDensity(
            context.resources,
            R.mipmap.ic_launcher_new,
            DisplayMetrics.DENSITY_XXXHIGH,
            context.theme
        )

        var bm = launcherDrawable!!.toBitmap(
            launcherDrawable.minimumWidth, launcherDrawable.minimumHeight
        ).asImageBitmap()

        val bmEnabled = packageManager.getComponentEnabledSetting(
            ComponentName(
                context, "com.daniebeler.pfpixelix.MainActivity"
            )
        )

        val launcherDrawableFief = ResourcesCompat.getDrawableForDensity(
            context.resources, R.mipmap.ic_launcher, DisplayMetrics.DENSITY_XXXHIGH, context.theme
        )

        var bmFief = launcherDrawableFief!!.toBitmap(
            launcherDrawable.minimumWidth, launcherDrawable.minimumHeight
        ).asImageBitmap()

        val bmFiefEnabled = packageManager.getComponentEnabledSetting(
            ComponentName(
                context, "com.daniebeler.pfpixelix.Fief"
            )
        )


        val launcherDrawable01 = ResourcesCompat.getDrawableForDensity(
            context.resources, R.mipmap.ic_launcher_01, DisplayMetrics.DENSITY_XXXHIGH, context.theme
        )

        var bm01 = launcherDrawable01!!.toBitmap(
            launcherDrawable.minimumWidth, launcherDrawable.minimumHeight
        ).asImageBitmap()

        val bm01Enabled = packageManager.getComponentEnabledSetting(
            ComponentName(
                context, "com.daniebeler.pfpixelix.Icon01"
            )
        )

        icons = listOf(
            IconWithName("com.daniebeler.pfpixelix.MainActivity", bm, bmEnabled == 1),
            IconWithName("com.daniebeler.pfpixelix.Icon01", bm01, bm01Enabled == 1),
            IconWithName("com.daniebeler.pfpixelix.Fief", bmFief, bmFiefEnabled == 1)
        )
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


            // Log and display a toast message indicating that the main logo is enabled
            Log.e("MainLogo", "Main Logo Enabled")
            Toast.makeText(context, "Main Logo Enabled.", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Log.e("Exceptionn", e.toString())
        }
    }
}

data class IconWithName(
    val name: String, val icon: ImageBitmap, val enabled: Boolean = false
)