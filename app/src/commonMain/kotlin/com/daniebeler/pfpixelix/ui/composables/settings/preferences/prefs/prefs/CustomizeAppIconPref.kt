package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import com.daniebeler.pfpixelix.utils.Navigate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.chevron_forward_outline
import pixelix.app.generated.resources.customize_app_icon
import pixelix.app.generated.resources.pixelix_logo

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