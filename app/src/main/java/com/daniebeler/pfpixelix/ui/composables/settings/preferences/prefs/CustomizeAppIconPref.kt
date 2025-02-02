package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun CustomizeAppIconPref(navController: NavController, closePreferenceDrawer: () -> Unit, @DrawableRes logo: Int) {
    SettingPref(leadingIcon = painterResource(logo),
        title = stringResource(id = R.string.customize_app_icon),
        trailingContent = R.drawable.chevron_forward_outline,
        onClick = {
            closePreferenceDrawer()
            Navigate.navigate("icon_selection_screen", navController)
        })
}

@Composable
fun CustomizeAppIconPref(navController: NavController, closePreferenceDrawer: () -> Unit, logo: ImageBitmap) {
    SettingPref(leadingIcon = logo,
        title = stringResource(id = R.string.customize_app_icon),
        trailingContent = R.drawable.chevron_forward_outline,
        onClick = {
            closePreferenceDrawer()
            Navigate.navigate("icon_selection_screen", navController)
        })
}

@Preview
@Composable
private fun CustomizeAppIconPrefPreview() {
    CustomizeAppIconPref(rememberNavController(), {}, R.drawable.pixelix_logo)
}