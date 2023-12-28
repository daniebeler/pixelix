package com.daniebeler.pixels.ui.composables.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixels.LoginActivity
import com.daniebeler.pixels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComposable(navController: NavController, viewModel: MainViewModel) {

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
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
                }
            )

        }
    ) {paddingValues ->
            Column (Modifier.padding(paddingValues)) {

                CustomSettingsElement(icon = Icons.Outlined.DoNotDisturbOn, text = "Muted Accounts", onClick = {
                    navController.navigate("muted_accounts_screen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                })

                CustomSettingsElement(icon = Icons.Outlined.Block, text = "Blocked Accounts", onClick = {
                    navController.navigate("blocked_accounts_screen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                })

                CustomSettingsElement(icon = Icons.AutoMirrored.Outlined.Logout, text = "Logout", onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        viewModel.logout()
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                })

        }

    }
}

@Composable
private fun CustomSettingsElement(icon: ImageVector, text: String, onClick: () -> Unit) {

    Row (verticalAlignment = Alignment.CenterVertically,
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

        Text(text = text)
    }
}