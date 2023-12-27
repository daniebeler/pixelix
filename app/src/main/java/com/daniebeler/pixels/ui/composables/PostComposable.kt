package com.daniebeler.pixels.ui.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixels.domain.model.Post


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostComposable(post: Post, navController: NavController, viewModel: PostViewModel = hiltViewModel()) {

    println("postincomposable")
    println(post)

    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(0) }

    viewModel.convertTime(post.createdAt)

    Column {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(start = 8.dp)
            .clickable(onClick = {
                navController.navigate("profile_screen/" + post.account.id) {
                    launchSingleTop = true
                    restoreState = true
                }
            })) {
            AsyncImage(
                model = post.account.avatar, contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
            )
            Column (modifier = Modifier.padding(start = 8.dp)) {
                Text(text = post.account.displayname)
                Text(text = viewModel.timeAgoString + " • @" + post.account.acct, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                showBottomSheet = 2
            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = ""
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (post.mediaAttachments[0].meta?.original?.aspect != null) {
            AsyncImage(model = post.mediaAttachments[0].url, contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(post.mediaAttachments[0].meta!!.original!!.aspect!!.toFloat()), contentScale = ContentScale.FillWidth)
        }
        else {
            AsyncImage(model = post.mediaAttachments[0].url, contentDescription = "",
                Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
        }

        Column (Modifier.padding(8.dp)) {
            Text(text = post.favouritesCount.toString() + " likes")

            HashtagsMentionsTextView(text = post.account.username + " " + post.content, onClick = {
                val newHastag = it
                navController.navigate("hashtag_timeline_screen/$newHastag") {
                    launchSingleTop = true
                    restoreState = true
                }
            })

            if (post.replyCount > 0) {
                TextButton(onClick = {
                    viewModel.loadReplies(post.account.id, post.id)
                    showBottomSheet = 1
                }) {
                    Text(text = "View " + post.replyCount + " comments")
                }
            }
        }
    }

    if (showBottomSheet > 0) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = 0
            },
            sheetState = sheetState
        ) {
            if (showBottomSheet == 1) {
                Column (
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp)) {
                    Text(text = post.content)
                    HorizontalDivider(Modifier.padding(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        items(viewModel.replies, key = {
                            it.id
                        }) { reply ->
                            HashtagsMentionsTextView(text = reply.content, onClick = {
                                println("clicked")
                                println(it)
                            })
                        }
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(bottom = 32.dp, start = 12.dp)
                ) {
                    Text(text = "Open in browser", Modifier.clickable {
                        openUrl(context, post.url)
                    })

                    Text(text = "Share this post", Modifier.clickable {
                        shareProfile(context, post.url)
                    })
                }
            }

        }
    }

}

private fun openUrl(context: Context, url: String){
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, Uri.parse(url))
}

private fun shareProfile(context: Context, url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}


@Composable
fun HashtagsMentionsTextView(text: String, modifier: Modifier = Modifier, onClick: (String) -> Unit) {

    val colorScheme = MaterialTheme.colorScheme
    val textStyle = SpanStyle(color = colorScheme.onBackground)
    val primaryStyle = SpanStyle(color = colorScheme.error)

    val hashtags = Regex("((?=[^\\w!])[#][\\u4e00-\\u9fa5\\w]+)")

    val annotatedStringList = remember {

        var lastIndex = 0
        val annotatedStringList = mutableStateListOf<AnnotatedString.Range<String>>()

        // Add a text range for hashtags
        for (match in hashtags.findAll(text)) {

            val start = match.range.first
            val end = match.range.last + 1
            val string = text.substring(start, end)

            if (start > lastIndex) {
                annotatedStringList.add(
                    AnnotatedString.Range(
                        text.substring(lastIndex, start),
                        lastIndex,
                        start,
                        "text"
                    )
                )
            }
            annotatedStringList.add(
                AnnotatedString.Range(string, start, end, "link")
            )
            lastIndex = end
        }

        // Add remaining text
        if (lastIndex < text.length) {
            annotatedStringList.add(
                AnnotatedString.Range(
                    text.substring(lastIndex, text.length),
                    lastIndex,
                    text.length,
                    "text"
                )
            )
        }
        annotatedStringList
    }

    // Build an annotated string
    val annotatedString = buildAnnotatedString {
        annotatedStringList.forEach {
            if (it.tag == "link") {
                pushStringAnnotation(tag = it.tag, annotation = it.item)
                withStyle(style = primaryStyle) { append(it.item) }
                pop()
            } else {
                withStyle(style = textStyle) { append(it.item) }
            }
        }
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        onClick = { position ->
            val annotatedStringRange =
                annotatedStringList.first { it.start < position && position < it.end }
            if (annotatedStringRange.tag == "link") onClick(annotatedStringRange.item)
        }
    )
}