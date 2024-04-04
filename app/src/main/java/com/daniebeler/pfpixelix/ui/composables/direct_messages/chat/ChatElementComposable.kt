package com.daniebeler.pfpixelix.ui.composables.direct_messages.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daniebeler.pfpixelix.domain.model.Message

@Composable
fun ConversationElementComposable(message: Message, navController: NavController) {
    var arrangement = Arrangement.Start
    var contentAlignment = Alignment.TopStart
    var backgroundColor = MaterialTheme.colorScheme.surfaceContainer
    var textColor = MaterialTheme.colorScheme.onSurface

    if (message.isAuthor) {
        arrangement = Arrangement.End
        contentAlignment = Alignment.TopEnd
        backgroundColor = MaterialTheme.colorScheme.primary
        textColor = MaterialTheme.colorScheme.onPrimary
    }

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(), horizontalArrangement = arrangement
    ) {

        Box(modifier = Modifier.fillMaxWidth(0.75f), contentAlignment = contentAlignment) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = backgroundColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(
                ) {
                    Text(text = message.text, color = textColor)

                    Row {
                        Text(text = message.timeAgo, color = textColor, fontSize = 10.sp)
                        if (message.seen) {
                            Icon(imageVector = Icons.Outlined.RemoveRedEye, contentDescription = null)
                        }
                    }
                }
            }
        }


    }


}