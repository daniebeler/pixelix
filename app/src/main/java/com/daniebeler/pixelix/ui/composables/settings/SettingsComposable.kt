package com.daniebeler.pixelix.ui.composables.settings

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.daniebeler.pixelix.LoginActivity
import com.daniebeler.pixelix.MainViewModel
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.CharacterIterator
import java.text.StringCharacterIterator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComposable(
    navController: NavController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val showLogoutDialog = remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(Unit) {
        getCacheSize(context, settingsViewModel)
        settingsViewModel.getVersionName(context)
    }

    Scaffold(contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(windowInsets = WindowInsets(0, 0, 0, 0),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.settings))
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

            CustomSettingsElement(icon = Icons.Outlined.FavoriteBorder,
                text = stringResource(R.string.liked_posts),
                onClick = {
                    Navigate().navigate("liked_posts_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.Bookmarks,
                text = stringResource(R.string.bookmarked_posts),
                onClick = {
                    Navigate().navigate("bookmarked_posts_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.Tag,
                text = stringResource(R.string.followed_hashtags),
                onClick = {
                    Navigate().navigate("followed_hashtags_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.DoNotDisturbOn,
                text = stringResource(R.string.muted_accounts),
                onClick = {
                    Navigate().navigate("muted_accounts_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.Block,
                text = stringResource(R.string.blocked_accounts),
                onClick = {
                    Navigate().navigate("blocked_accounts_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.Dns,
                text = stringResource(R.string.about_this_instance),
                onClick = {
                    Navigate().navigate("about_instance_screen", navController)
                })

            CustomSettingsElement(icon = Icons.Outlined.Save,
                text = stringResource(R.string.clear_cache),
                smallText = settingsViewModel.cacheSize,
                onClick = {
                    deleteCache(context, viewModel = settingsViewModel)
                })

            CustomSettingsElement(icon = Icons.AutoMirrored.Outlined.Logout, text = stringResource(
                R.string.logout
            ), onClick = {
                showLogoutDialog.value = true
            })


            HorizontalDivider(modifier = Modifier.padding(12.dp))

            Text(
                text = "Pixelix v" + settingsViewModel.versionName,
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
                        mainViewModel.logout()
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

private fun getCacheSize(context: Context, settingsViewModel: SettingsViewModel) {
    val cacheInbytes: Long =
        context.cacheDir.walkBottomUp().fold(0L, { acc, file -> acc + file.length() })

    settingsViewModel.cacheSize = humanReadableByteCountSI(cacheInbytes)
}

fun humanReadableByteCountSI(bytes: Long): String {
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


private fun deleteCache(context: Context, viewModel: SettingsViewModel) {
    context.cacheDir.deleteRecursively()
    getCacheSize(context, viewModel)
}

@Composable
private fun CustomSettingsElement(
    icon: ImageVector, text: String, smallText: String = "", onClick: () -> Unit
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
            Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp)
            }
        }
    }
}