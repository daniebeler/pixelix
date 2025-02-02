package com.daniebeler.pfpixelix.ui.composables.post

import android.content.Context
import android.os.Build
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElement
import com.daniebeler.pfpixelix.utils.Navigate
import com.daniebeler.pfpixelix.utils.Share

@Composable
fun ShareBottomSheet(
    context: Context,
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
        humanReadableVisibility = if (post.visibility == Constants.AUDIENCE_PUBLIC) {
            context.resources.getString(R.string.audience_public)
        } else if (post.visibility == Constants.AUDIENCE_UNLISTED) {
            context.resources.getString(R.string.unlisted)
        } else if (post.visibility == Constants.AUDIENCE_FOLLOWERS_ONLY) {
            context.resources.getString(R.string.followers_only)
        } else {
            post.visibility
        }
    }


    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.eye_outline),
                contentDescription = "",
                Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = stringResource(R.string.visibility_x, humanReadableVisibility))
        }
        if (mediaAttachment?.license != null) {
            ButtonRowElement(icon = R.drawable.document_text_outline, text = stringResource(
                R.string.license, mediaAttachment.license.title
            ), onClick = {
                viewModel.openUrl(mediaAttachment.license.url)
            })
        }

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElement(icon = R.drawable.open_outline, text = stringResource(
            R.string.open_in_browser
        ), onClick = {
            viewModel.openUrl(url)
        })

        ButtonRowElement(icon = R.drawable.share_social_outline,
            text = stringResource(R.string.share_this_post),
            onClick = {
                Share.shareText(context, url)
            })

        if (mediaAttachment != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && mediaAttachment.type == "image") {
            ButtonRowElement(icon = R.drawable.cloud_download_outline,
                text = stringResource(R.string.download_image),
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
                icon = R.drawable.pencil_outline,
                text = stringResource(R.string.edit_post),
                onClick = {
                    Navigate.navigate("edit_post_screen/${post.id}", navController = navController)
                }
            )
            ButtonRowElement(
                icon = R.drawable.trash_outline,
                text = stringResource(R.string.delete_this_post),
                onClick = {
                    viewModel.deleteDialog = post.id
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
