package com.daniebeler.pfpixelix.ui.composables.post

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Visibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.common.Constants
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.ui.composables.ButtonRowElement
import com.daniebeler.pfpixelix.utils.Share

@Composable
fun ShareBottomSheet(
    context: Context, url: String, minePost: Boolean, viewModel: PostViewModel, post: Post
) {

    var humanReadableVisibility by remember {
        mutableStateOf("")
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
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "",
                Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text = stringResource(R.string.visibility_x, humanReadableVisibility))
        }

        HorizontalDivider(Modifier.padding(12.dp))

        ButtonRowElement(icon = Icons.Outlined.OpenInBrowser, text = stringResource(
            R.string.open_in_browser
        ), onClick = {
            viewModel.openUrl(context, url)
        })

        ButtonRowElement(icon = Icons.Outlined.Share,
            text = stringResource(R.string.share_this_post),
            onClick = {
                Share.shareText(context, url)
            })

        ButtonRowElement(icon = Icons.Outlined.Download,
            text = "Download image",
            onClick = {
                viewModel.saveImage("fiefname", "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg", context)
            })

        if (minePost) {
            ButtonRowElement(
                icon = Icons.Outlined.Delete,
                text = stringResource(R.string.delete_this_post),
                onClick = {
                    viewModel.deleteDialog = post.id
                },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
