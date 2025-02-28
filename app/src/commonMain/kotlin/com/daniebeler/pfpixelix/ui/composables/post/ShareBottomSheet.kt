package com.daniebeler.pfpixelix.ui.composables.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.service.platform.PlatformFeatures
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElement
import com.daniebeler.pfpixelix.utils.KmpContext
import com.daniebeler.pfpixelix.utils.Navigate
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.audience_public
import pixelix.app.generated.resources.cloud_download_outline
import pixelix.app.generated.resources.delete_this_post
import pixelix.app.generated.resources.document_text_outline
import pixelix.app.generated.resources.download_image
import pixelix.app.generated.resources.edit_post
import pixelix.app.generated.resources.eye_outline
import pixelix.app.generated.resources.followers_only
import pixelix.app.generated.resources.license
import pixelix.app.generated.resources.open_in_browser
import pixelix.app.generated.resources.open_outline
import pixelix.app.generated.resources.pencil_outline
import pixelix.app.generated.resources.share_social_outline
import pixelix.app.generated.resources.share_this_post
import pixelix.app.generated.resources.trash_outline
import pixelix.app.generated.resources.unlisted
import pixelix.app.generated.resources.visibility_x

@Composable
fun ShareBottomSheet(
    context: KmpContext,
    url: String,
    minePost: Boolean,
    viewModel: PostViewModel,
    post: Post,
    currentMediaAttachmentNumber: Int,
    navController: NavController
) {

    var humanReadableVisibility by remember {
        mutableStateOf("")
    }

    val mediaAttachment: MediaAttachment? = viewModel.post?.mediaAttachments?.let { attachments ->
        if (attachments.isNotEmpty() && currentMediaAttachmentNumber in attachments.indices) {
            attachments[currentMediaAttachmentNumber]
        } else {
            null
        }
    }

    LaunchedEffect(Unit) {
        humanReadableVisibility = when (post.visibility) {
            Constants.AUDIENCE_PUBLIC -> getString(Res.string.audience_public)
            Constants.AUDIENCE_UNLISTED -> getString(Res.string.unlisted)
            Constants.AUDIENCE_FOLLOWERS_ONLY -> getString(Res.string.followers_only)
            else -> post.visibility
        }
    }


    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.eye_outline),
                contentDescription = "",
                Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = stringResource(Res.string.visibility_x, humanReadableVisibility))
        }
        if (mediaAttachment?.license != null) {
            ButtonRowElement(icon = Res.drawable.document_text_outline, text = stringResource(
                Res.string.license, mediaAttachment.license.title
            ), onClick = {
                viewModel.openUrl(mediaAttachment.license.url, context)
            })
        }

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElement(icon = Res.drawable.open_outline, text = stringResource(
            Res.string.open_in_browser
        ), onClick = {
            viewModel.openUrl(url, context)
        })

        ButtonRowElement(icon = Res.drawable.share_social_outline,
            text = stringResource(Res.string.share_this_post),
            onClick = {
                viewModel.shareText(url)
            })

        if (mediaAttachment != null && PlatformFeatures.downloadToGallery && mediaAttachment.type == "image") {
            ButtonRowElement(icon = Res.drawable.cloud_download_outline,
                text = stringResource(Res.string.download_image),
                onClick = {

                    viewModel.saveImage(
                        post.account.username,
                        viewModel.post!!.mediaAttachments[currentMediaAttachmentNumber].url!!,
                        context
                    )
                })
        }

        if (minePost) {
            HorizontalDivider(Modifier.padding(12.dp))

            ButtonRowElement(
                icon = Res.drawable.pencil_outline,
                text = stringResource(Res.string.edit_post),
                onClick = {
                    Navigate.navigate("edit_post_screen/${post.id}", navController = navController)
                }
            )
            ButtonRowElement(
                icon = Res.drawable.trash_outline,
                text = stringResource(Res.string.delete_this_post),
                onClick = {
                    viewModel.deleteDialog = post.id
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
