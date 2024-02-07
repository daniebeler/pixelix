package com.daniebeler.pixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.utils.Navigate

@Composable
fun ModalBottomSheetContent(navController: NavController, instanceDomain: String, closeBottomSheet: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {

        CustomSettingsElement(icon = Icons.Outlined.Settings,
            text = stringResource(R.string.settings),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("preferences_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.liked_posts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("liked_posts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Bookmarks,
            text = stringResource(R.string.bookmarked_posts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("bookmarked_posts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Tag,
            text = stringResource(R.string.followed_hashtags),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("followed_hashtags_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.DoNotDisturbOn,
            text = stringResource(R.string.muted_accounts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("muted_accounts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Block,
            text = stringResource(R.string.blocked_accounts),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("blocked_accounts_screen", navController)
            })

        CustomSettingsElement(icon = Icons.Outlined.Dns,
            text = stringResource(R.string.about_x, instanceDomain),
            onClick = {
                closeBottomSheet()
                Navigate().navigate("about_instance_screen", navController)
            })

    }
}