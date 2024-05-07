package com.daniebeler.pfpixelix.ui.composables.settings.icon_selection

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IconSelectionViewModel @Inject constructor(

): ViewModel() {

    private fun changeIconToMain(context: Context) {
        try {
            // Get the package manager instance
            val packageManager = context.packageManager

            // Disable the New Year component
            val newYearComponent = ComponentName(context, "com.daniebeler.NewYearAlias")
            packageManager.setComponentEnabledSetting(
                newYearComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )

            // Disable the Christmas component
            val christmasComponent = ComponentName(context, "com.exampledynamicicon.ChristmasAlias")
            packageManager.setComponentEnabledSetting(
                christmasComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )

            // Enable the main activity component
            val mainActivityComponent = ComponentName(context, "com.exampledynamicicon.SplashActivity")
            packageManager.setComponentEnabledSetting(
                mainActivityComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            // Log and display a toast message indicating that the main logo is enabled
            Log.e("MainLogo", "Main Logo Enabled")
            Toast.makeText(context.applicationContext, "Main Logo Enabled.", Toast.LENGTH_SHORT)
                .show()
        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.toString())
        }
    }
}