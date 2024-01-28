package com.daniebeler.pixelix.ui.composables.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Tag
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pixelix.LoginActivity
import com.daniebeler.pixelix.MainViewModel
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.utils.Navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComposable(navController: NavController, viewModel: MainViewModel) {

    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
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
                }
            )

        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {

            CustomSettingsElement(
                icon = Icons.Outlined.FavoriteBorder,
                text = stringResource(R.string.liked_posts),
                onClick = {
                    Navigate().navigate("liked_posts_screen", navController)
                })

            CustomSettingsElement(
                icon = Icons.Outlined.Bookmarks,
                text = stringResource(R.string.bookmarked_posts),
                onClick = {
                    Navigate().navigate("bookmarked_posts_screen", navController)
                })

            CustomSettingsElement(
                icon = Icons.Outlined.Tag,
                text = stringResource(R.string.followed_hashtags),
                onClick = {
                    Navigate().navigate("followed_hashtags_screen", navController)
                })

            CustomSettingsElement(
                icon = Icons.Outlined.DoNotDisturbOn,
                text = stringResource(R.string.muted_accounts),
                onClick = {
                    Navigate().navigate("muted_accounts_screen", navController)
                })

            CustomSettingsElement(
                icon = Icons.Outlined.Block,
                text = stringResource(R.string.blocked_accounts),
                onClick = {
                    Navigate().navigate("blocked_accounts_screen", navController)
                })

            CustomSettingsElement(
                icon = Icons.Outlined.Dns,
                text = stringResource(R.string.about_this_instance),
                onClick = {
                    Navigate().navigate("about_instance_screen", navController)
                })

            CustomSettingsElement(icon = Icons.AutoMirrored.Outlined.Logout, text = stringResource(
                R.string.logout
            ), onClick = {
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

        Text(text = text)
    }
}