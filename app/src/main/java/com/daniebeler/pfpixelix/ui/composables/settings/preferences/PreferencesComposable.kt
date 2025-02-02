package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.LoginActivity
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElement
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElementWithRoundedImage
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.FocusModePref
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.HideAltTextButtonPref
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.HideSensitiveContentPref
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.ThemePref
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.UseInAppBrowserPref
import com.daniebeler.pfpixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.CharacterIterator
import java.text.StringCharacterIterator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesComposable(
    navController: NavController,
    viewModel: PreferencesViewModel = hiltViewModel(key = "preferences-viewmodel-key")
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val context = LocalContext.current

    val showLogoutDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getCacheSize(context, viewModel)
        viewModel.getVersionName(context)
        viewModel.getAppIcon(context)
    }

    Scaffold(contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.settings), fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_back_outline),
                            contentDescription = ""
                        )
                    }
                })
        }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /*SwitchRowItem(
                icon = R.drawable.eye_off_outline,
                text = stringResource(R.string.hide_sensitive_content),
                isChecked = viewModel.isSensitiveContentHidden
            ) { checked -> viewModel.storeHideSensitiveContent(checked) }*/

            HideSensitiveContentPref()

            HideAltTextButtonPref()

            FocusModePref()

            UseInAppBrowserPref()

            ButtonRowElement(icon = R.drawable.sync_outline,
                text = stringResource(R.string.repost_settings),
                onClick = { viewModel.openRepostSettings() })

            HorizontalDivider(modifier = Modifier.padding(12.dp))

            /*ButtonRowElement(icon = R.drawable.color_palette_outline,
                text = stringResource(R.string.app_theme),
                smallText = getThemeString(themeViewModel.currentTheme.theme),
                onClick = { showThemeDialog.value = true })*/

            ThemePref()

            if (viewModel.appIcon == null) {
                ButtonRowElementWithRoundedImage(
                    icon = R.drawable.pixelix_logo,
                    text = stringResource(R.string.customize_app_icon),
                    onClick = {
                        Navigate.navigate("icon_selection_screen", navController)
                    })
            } else {
                ButtonRowElement(
                    icon = viewModel.appIcon!!,
                    text = stringResource(R.string.customize_app_icon),
                    onClick = {
                        Navigate.navigate("icon_selection_screen", navController)
                    })
            }

            HorizontalDivider(modifier = Modifier.padding(12.dp))

            ButtonRowElement(icon = R.drawable.save_outline,
                text = stringResource(R.string.clear_cache),
                smallText = viewModel.cacheSize,
                onClick = {
                    deleteCache(context, viewModel = viewModel)
                })

            ButtonRowElement(icon = R.drawable.settings_outline,
                text = stringResource(R.string.more_settings),
                onClick = {
                    viewModel.openMoreSettingsPage()
                })

            ButtonRowElement(
                icon = R.drawable.log_out_outline, text = stringResource(
                    R.string.logout
                ), onClick = {
                    showLogoutDialog.value = true
                }, color = MaterialTheme.colorScheme.error
            )


            HorizontalDivider(modifier = Modifier.padding(12.dp))

            Text(
                text = "Pixelix v" + viewModel.versionName,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }

        if (showLogoutDialog.value) {
            AlertDialog(title = {
                Text(text = stringResource(R.string.logout_questionmark))
            }, text = {
                Text(text = stringResource(R.string.are_you_sure_you_want_to_log_out))
            }, onDismissRequest = {
                showLogoutDialog.value = false
            }, confirmButton = {
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.logout()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                }) {
                    Text(stringResource(R.string.logout))
                }
            }, dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog.value = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            })
        }
    }
}

@Composable
private fun getThemeString(theme: String): String {
    return when (theme) {
        "system" -> stringResource(R.string.theme_system)
        "dark" -> stringResource(R.string.theme_dark)
        "light" -> stringResource(R.string.theme_light)
        else -> ""
    }
}


private fun getCacheSize(context: Context, settingsViewModel: PreferencesViewModel) {
    val cacheInbytes: Long =
        context.cacheDir.walkBottomUp().fold(0L, { acc, file -> acc + file.length() })

    settingsViewModel.cacheSize = humanReadableByteCountSI(cacheInbytes)
}

private fun humanReadableByteCountSI(bytes: Long): String {
    var bytes = bytes
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current())
}


private fun deleteCache(context: Context, viewModel: PreferencesViewModel) {
    context.cacheDir.deleteRecursively()
    getCacheSize(context, viewModel)
}