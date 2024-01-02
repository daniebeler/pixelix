package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.daniebeler.pixelix.domain.model.Account
import com.daniebeler.pixelix.domain.model.Relationship

@Composable
fun CustomAccount(account: Account, relationship: Relationship?, navController: NavController) {
    Row (modifier = Modifier
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .fillMaxWidth()
        .clickable {
            navController.navigate("profile_screen/" + account.id) {
                launchSingleTop = true
                restoreState = true
            }
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = account.avatar, contentDescription = "",
            modifier = Modifier
                .height(46.dp)
                .width(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = "@${account.username}")
            Text(text = "${account.followersCount} followers", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.weight(1f))

        if (relationship != null) {
            if (relationship.following) {
                Button(onClick = { /*TODO*/ },) {
                    Text(text = "unfollow")
                }
            } else {
                Button(onClick = { /*TODO*/ },) {
                    Text(text = "follow")
                }
            }

        }
    }
}