package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun ModalBottomSheetContent(
    navController: NavController, instanceDomain: String, closeBottomSheet: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
            .padding(bottom = 12.dp)
    ) {

        CustomBottomSheetElement(icon = Icons.Outlined.Settings,
            text = stringResource(R.string.settings),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("preferences_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.Edit,
            text = stringResource(R.string.edit_profile),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("edit_profile_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.liked_posts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("liked_posts_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.Bookmarks,
            text = stringResource(R.string.bookmarked_posts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("bookmarked_posts_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.Tag,
            text = stringResource(R.string.followed_hashtags),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("followed_hashtags_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.DoNotDisturbOn,
            text = stringResource(R.string.muted_accounts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("muted_accounts_screen", navController)
            })

        CustomBottomSheetElement(icon = Icons.Outlined.Block,
            text = stringResource(R.string.blocked_accounts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("blocked_accounts_screen", navController)
            })

        CustomBottomSheetElement(icon = R.drawable.pixelfed_logo,
            text = stringResource(R.string.about_x, instanceDomain),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("about_instance_screen", navController)
            })

        CustomBottomSheetElement(icon = R.drawable.pixelix_logo, text = stringResource(id = R.string.about_pixelix), onClick = {
            closeBottomSheet()
            Navigate.navigate("about_pixelix_screen", navController)
        })

    }
}

@Composable
private fun CustomBottomSheetElement(
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

@Composable
private fun CustomBottomSheetElement(
    @DrawableRes icon: Int, text: String, smallText: String = "", onClick: () -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
            Modifier
                .padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
                .height(24.dp)
                .width(24.dp)
                .clip(
                    CircleShape
                )
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