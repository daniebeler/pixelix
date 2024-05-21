package com.daniebeler.pfpixelix.ui.composables.settings.preferences

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Adjust
import androidx.compose.material.icons.outlined.NoAdultContent
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.LoginActivity
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.ThemeViewModel
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
    themeViewModel: ThemeViewModel = hiltViewModel(key = "Theme"),
    viewModel: PreferencesViewModel = hiltViewModel(key = "preferences-viewmodel-key")
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val context = LocalContext.current

    val showLogoutDialog = remember { mutableStateOf(false) }
    val showThemeDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getCacheSize(context, viewModel)
        viewModel.getVersionName(context)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.settings), fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                })
        }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            SwitchPreferencesItem(
                Icons.Outlined.NoAdultContent,
                stringResource(R.string.hide_sensitive_content),
                viewModel.isSensitiveContentHidden
            ) { checked -> viewModel.storeHideSensitiveContent(checked) }

            SwitchPreferencesItem(
                Icons.Outlined.OpenInBrowser,
                stringResource(R.string.use_in_app_browser),
                viewModel.isUsingInAppBrowser
            ) { checked -> viewModel.storeUseInAppBrowser(checked) }

            HorizontalDivider(modifier = Modifier.padding(12.dp))

            ButtonPreferencesItem(icon = Icons.Outlined.Palette,
                text = stringResource(R.string.app_theme),
                smallText = getThemeString(themeViewModel.currentTheme.theme),
                onClick = { showThemeDialog.value = true })

            ButtonPreferencesItem(icon = Icons.Outlined.Adjust, text = stringResource(R.string.customize_app_icon), onClick = {
                Navigate.navigate("icon_selection_screen", navController)
            })

            HorizontalDivider(modifier = Modifier.padding(12.dp))

            ButtonPreferencesItem(icon = Icons.Outlined.Save,
                text = stringResource(R.string.clear_cache),
                smallText = viewModel.cacheSize,
                onClick = {
                    deleteCache(context, viewModel = viewModel)
                })

            ButtonPreferencesItem(icon = Icons.Outlined.Settings,
                text = stringResource(R.string.more_settings),
                onClick = {
                    viewModel.openMoreSettingsPage(context)
                })

            ButtonPreferencesItem(
                icon = Icons.AutoMirrored.Outlined.Logout, text = stringResource(
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

        if (showThemeDialog.value) {
            val themeOptions = listOf("system", "dark", "light")
            AlertDialog(title = {
                Text(text = stringResource(R.string.app_theme))
            }, text = {
                Column(Modifier.selectableGroup()) {
                    themeOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == themeViewModel.currentTheme.theme),
                                    onClick = {
                                        showThemeDialog.value = false
                                        themeViewModel.storeTheme(text)
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == themeViewModel.currentTheme.theme),
                                onClick = null
                            )
                            Text(
                                text = getThemeString(text),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }, onDismissRequest = {
                showThemeDialog.value = false
            }, confirmButton = {}, dismissButton = {
                TextButton(onClick = {
                    showThemeDialog.value = false
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

@Composable
private fun SwitchPreferencesItem(
    icon: ImageVector, text: String, isChecked: Boolean, onCheckedChange: (checked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text)
        }
        Switch(checked = isChecked, onCheckedChange = { onCheckedChange(it) })
    }
}

@Composable
private fun ButtonPreferencesItem(
    icon: ImageVector,
    text: String,
    smallText: String = "",
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp),
            tint = color
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text, color = color)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp, color = color)
            }
        }
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