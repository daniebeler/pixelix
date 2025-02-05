package com.daniebeler.pfpixelix.ui.composables.profile.own_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElement
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElementWithRoundedImage
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun ModalBottomSheetContent(
    navController: NavController, instanceDomain: String, appIcon: ImageBitmap?, closeBottomSheet: () -> Unit, openPreferencesDrawer: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
            .padding(bottom = 12.dp)
    ) {

        ButtonRowElement(
            icon = Res.drawable.settings_outline,
            text = stringResource(Res.string.settings),
            onClick = {
                closeBottomSheet()
                openPreferencesDrawer()
            })

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElement(icon = Res.drawable.heart_outline,
            text = stringResource(Res.string.liked_posts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("liked_posts_screen", navController)
            })

        ButtonRowElement(icon = Res.drawable.bookmarks_outline,
            text = stringResource(Res.string.bookmarked_posts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("bookmarked_posts_screen", navController)
            })

        ButtonRowElement(icon = Res.drawable.hash,
            text = stringResource(Res.string.followed_hashtags),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("followed_hashtags_screen", navController)
            })

        ButtonRowElement(icon = Res.drawable.remove_circle_outline,
            text = stringResource(Res.string.muted_accounts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("muted_accounts_screen", navController)
            })

        ButtonRowElement(icon = Res.drawable.remove_circle_outline,
            text = stringResource(Res.string.blocked_accounts),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("blocked_accounts_screen", navController)
            })

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElementWithRoundedImage(icon = Res.drawable.pixelfed_logo,
            text = stringResource(Res.string.about_x, instanceDomain),
            onClick = {
                closeBottomSheet()
                Navigate.navigate("about_instance_screen", navController)
            })

        if (appIcon == null) {
            ButtonRowElementWithRoundedImage(icon = Res.drawable.pixelix_logo,
                text = stringResource(Res.string.about_pixelix),
                onClick = {
                    closeBottomSheet()
                    Navigate.navigate("about_pixelix_screen", navController)
                })
        } else {
            ButtonRowElement(icon = appIcon,
                text = stringResource(Res.string.about_pixelix),
                onClick = {
                    closeBottomSheet()
                    Navigate.navigate("about_pixelix_screen", navController)
                })
        }

    }
}