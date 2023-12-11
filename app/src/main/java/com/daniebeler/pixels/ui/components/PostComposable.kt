package com.daniebeler.pixels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daniebeler.pixels.models.api.CountryRepository
import com.daniebeler.pixels.models.api.CountryRepositoryImpl
import com.daniebeler.pixels.models.api.Post
import com.daniebeler.pixels.models.api.Reply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostComposable(post: Post) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var replies by remember {
        mutableStateOf(emptyList<Reply>())
    }

    val repository: CountryRepository = CountryRepositoryImpl()


    Column {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 8.dp)) {
            AsyncImage(
                model = post.account.avatar, contentDescription = "",
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
            )
            Text(text = post.account.username, modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(model = post.mediaAttachments[0].url, contentDescription = "",
            Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth)

        Column (Modifier.padding(8.dp)) {
            Text(text = post.likes.toString() + " likes")
            Text(text = post.account.username + " " + post.content)

            if (post.replyCount > 0) {
                TextButton(onClick = {

                    CoroutineScope(Dispatchers.Default).launch {
                        replies = repository.getReplies(post.account.id, post.id)
                        println("fof")
                        println(replies.toString())
                    }
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
            Column (Modifier.fillMaxSize()) {
                Text(text = post.content)


                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    items(replies) { reply ->
                        Text(text = reply.content)
                    }
                }
            }
        }
    }

}