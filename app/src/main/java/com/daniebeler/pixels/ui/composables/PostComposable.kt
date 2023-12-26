package com.daniebeler.pixels.ui.composables

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.daniebeler.pixels.domain.model.Reply
import com.daniebeler.pixels.utils.TimeAgo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostComposable(post: Post, navController: NavController, viewModel: PostViewModel = hiltViewModel()) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                    showBottomSheet = true
                }) {
                    Text(text = "View " + post.replyCount + " comments")
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
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
        }
    }

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