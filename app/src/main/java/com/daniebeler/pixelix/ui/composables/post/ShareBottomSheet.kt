package com.daniebeler.pixelix.ui.composables.post

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.pixelix.R
import com.daniebeler.pixelix.domain.model.Post
import com.daniebeler.pixelix.utils.Navigate
import com.daniebeler.pixelix.utils.Share

@Composable
fun ShareBottomSheet(
    context: Context, url: String, minePost: Boolean, viewModel: PostViewModel, post: Post
) {
    Column(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        CustomBottomSheetElement(icon = Icons.Outlined.OpenInBrowser, text = stringResource(
            R.string.open_in_browser
        ), onClick = {
            Navigate().openUrlInApp(context, url)
        })

        CustomBottomSheetElement(icon = Icons.Outlined.Share,
            text = stringResource(R.string.share_this_post),
            onClick = {
                Share().shareText(context, url)
            })

        if (minePost) {
            CustomBottomSheetElement(icon = Icons.Outlined.Delete,
                text = stringResource(R.string.delete_this_post),
                onClick = {
                    viewModel.deleteDialog = post.id
                })
        }
    }
}