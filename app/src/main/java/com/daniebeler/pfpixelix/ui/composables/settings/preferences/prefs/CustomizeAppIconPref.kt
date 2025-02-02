package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun CustomizeAppIconPref(navController: NavController, @DrawableRes logo: Int) {
    SettingPref(leadingIcon = painterResource(logo),
        title = stringResource(id = R.string.customize_app_icon),
        trailingContent = R.drawable.chevron_back_outline,
        onClick = {
            Navigate.navigate("icon_selection_screen", navController)
        })
}

@Composable
fun CustomizeAppIconPref(navController: NavController, logo: ImageBitmap) {
    SettingPref(leadingIcon = logo,
        title = stringResource(id = R.string.customize_app_icon),
        trailingContent = R.drawable.chevron_back_outline,
        onClick = {
            Navigate.navigate("icon_selection_screen", navController)
        })
}

