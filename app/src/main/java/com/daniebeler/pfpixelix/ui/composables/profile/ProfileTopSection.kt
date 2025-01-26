package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.ui.composables.hashtagMentionText.HashtagsMentionsTextView
import com.daniebeler.pfpixelix.utils.Navigate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun ProfileTopSection(
    account: Account?,
    relationship: Relationship?,
    navController: NavController,
    openUrl: (url: String) -> Unit
) {
    if (account != null) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = account.avatar,
                    error = painterResource(id = R.drawable.default_avatar),
                    contentDescription = "",
                    modifier = Modifier
                        .height(76.dp)
                        .width(76.dp)
                        .clip(CircleShape)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format(Locale.GERMANY, "%,d", account.postsCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = stringResource(R.string.posts), fontSize = 12.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            Navigate.navigate(
                                "followers_screen/" + "followers/" + account.id, navController
                            )
                        }) {
                        Text(
                            text = String.format(Locale.GERMANY, "%,d", account.followersCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = stringResource(R.string.followers), fontSize = 12.sp)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            Navigate.navigate(
                                "followers_screen/" + "following/" + account.id, navController
                            )
                        }) {
                        Text(
                            text = String.format(Locale.GERMANY, "%,d", account.followingCount),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = stringResource(R.string.following), fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = account.displayname ?: account.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (account.locked) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (account.isAdmin) {
                        ProfileBadge(text = stringResource(id = R.string.admin))
                    }
                    if (relationship != null && relationship.followedBy) {
                        ProfileBadge(text = stringResource(R.string.follows_you))
                    }

                    if (relationship != null && relationship.muting) {
                        ProfileBadge(
                            text = stringResource(R.string.muted),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if (relationship != null && relationship.blocking) {
                        ProfileBadge(
                            text = stringResource(R.string.blocked),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }


            if (account.pronouns.isNotEmpty()) {
                Text(
                    text = account.pronouns.joinToString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (account.note.isNotBlank()) {
                HashtagsMentionsTextView(text = account.note,
                    mentions = null,
                    navController = navController,
                    openUrl = { url -> openUrl(url) })
            }

            account.website?.let {
                Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = account.website.toString().substringAfter("https://"),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = { openUrl(account.website.toString()) })
                    )
                }
            }

            if (account.createdAt.isNotBlank()) {
                val date: LocalDate = LocalDate.parse(account.createdAt.substringBefore("T"))
                val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                val formatted = date.format(formatter)
                Text(
                    text = "Joined $formatted",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileBadge(text: String, color: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Box(
        Modifier
            .border(
                BorderStroke(1.dp, color), shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 6.dp)
    ) {
        Text(text = text, fontSize = 9.sp, color = color)
    }
}