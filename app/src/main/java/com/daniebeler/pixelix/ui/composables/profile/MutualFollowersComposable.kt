package com.daniebeler.pixelix.ui.composables.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.daniebeler.pixelix.R

@Composable
fun MutualFollowersComposable(mutualFollowersState: MutualFollowersState) {

    if (mutualFollowersState.mutualFollowers.isNotEmpty()) {

        val listSize = mutualFollowersState.mutualFollowers.size

        val annotatedString = buildAnnotatedString {
            append(stringResource(R.string.followed_by) + " ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(mutualFollowersState.mutualFollowers.first().username)
            }

            if (listSize == 2) {
                append(" " + stringResource(R.string.and) + " ")
            }
            if (listSize > 2) {
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(mutualFollowersState.mutualFollowers[1].username)
                }
            }

            if (listSize == 3) {
                append(" " + stringResource(R.string.and) + " ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(mutualFollowersState.mutualFollowers[2].username)
                }
            }
            if (listSize > 3) {
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(mutualFollowersState.mutualFollowers[2].username)
                }
                append(" " + stringResource(id = R.string.and) + " ")
                append((listSize - 3).toString())
                if (listSize == 4) {
                    append(" " + stringResource(R.string.other))
                } else {
                    append(" " + stringResource(R.string.others))
                }
            }


        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                AsyncImage(
                    model = mutualFollowersState.mutualFollowers.first().avatar,
                    contentDescription = "",
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.background
                        )
                )

                if (listSize > 1) {
                    Row {
                        Spacer(modifier = Modifier.width(18.dp))
                        AsyncImage(
                            model = mutualFollowersState.mutualFollowers[1].avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(36.dp)
                                .width(36.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.background
                                )
                        )
                    }
                }
                if (listSize > 2) {
                    Row {
                        Spacer(modifier = Modifier.width(36.dp))
                        AsyncImage(
                            model = mutualFollowersState.mutualFollowers[2].avatar,
                            contentDescription = "",
                            modifier = Modifier
                                .height(36.dp)
                                .width(36.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.background
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = annotatedString, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}