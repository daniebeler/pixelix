package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniebeler.pfpixelix.LoginActivity
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.SettingPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LogoutPref(logout: () -> Unit) {
    val showAlert = remember { mutableStateOf(false) }

    LogoutAlert(showAlert, logout)

    SettingPref(
        leadingIcon = R.drawable.log_out_outline,
        title = stringResource(id = R.string.logout),
        trailingContent = null,
        onClick = { showAlert.value = true },
        textColor = MaterialTheme.colorScheme.error
    )
}

@Preview
@Composable
fun LogoutPrefPreview() {
    LogoutPref { }
}

@Composable
fun LogoutAlert(show: MutableState<Boolean>, logout: () -> Unit) {
    val context = LocalContext.current
    if (show.value) {
        AlertDialog(title = {
            Text(text = stringResource(R.string.logout_questionmark))
        }, text = {
            Text(text = stringResource(R.string.are_you_sure_you_want_to_log_out))
        }, onDismissRequest = {
            show.value = false
        }, confirmButton = {
            TextButton(onClick = {
                CoroutineScope(Dispatchers.Default).launch {
                    logout()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }
            }) {
                Text(stringResource(R.string.logout))
            }
        }, dismissButton = {
            TextButton(onClick = {
                show.value = false
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        })
    }
}

@Preview
@Composable
private fun LogoutAlertPreview() {
    val showAlert = remember { mutableStateOf(true) }
    LogoutAlert(show = showAlert) { }
}