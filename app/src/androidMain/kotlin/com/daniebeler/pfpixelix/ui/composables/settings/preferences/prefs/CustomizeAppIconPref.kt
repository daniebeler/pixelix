package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import com.daniebeler.pfpixelix.utils.Navigate
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun CustomizeAppIconPref(navController: NavController, closePreferenceDrawer: () -> Unit, logo: DrawableResource) {
    SettingPref(leadingIcon = painterResource(logo),
        title = stringResource(Res.string.customize_app_icon),
        trailingContent = Res.drawable.chevron_forward_outline,
        onClick = {
            closePreferenceDrawer()
            Navigate.navigate("icon_selection_screen", navController)
        })
}

@Composable
fun CustomizeAppIconPref(navController: NavController, closePreferenceDrawer: () -> Unit, logo: ImageBitmap) {
    SettingPref(leadingIcon = logo,
        title = stringResource(Res.string.customize_app_icon),
        trailingContent = Res.drawable.chevron_forward_outline,
        onClick = {
            closePreferenceDrawer()
            Navigate.navigate("icon_selection_screen", navController)
        })
}

@Preview
@Composable
private fun CustomizeAppIconPrefPreview() {
    CustomizeAppIconPref(rememberNavController(), {}, Res.drawable.pixelix_logo)
}