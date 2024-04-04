package com.daniebeler.pfpixelix.ui.composables.direct_messages.conversations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Conversation
import com.daniebeler.pfpixelix.domain.model.Notification
import com.daniebeler.pfpixelix.utils.Navigate

@Composable
fun ConversationElementComposable(conversation: Conversation, navController: NavController) {

    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = conversation.accounts.first().avatar, contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    Navigate.navigate("chat/" + conversation.accounts.first().id, navController)
                }
            ) {
                Text(text = conversation.accounts.first().username, fontWeight = FontWeight.Bold)

               // Text(text = text, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}