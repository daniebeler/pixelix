package com.daniebeler.pixelix.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.pixelix.R

@Composable
fun FollowButton(
    firstLoaded: Boolean,
    isLoading: Boolean,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onUnFollowClick: () -> Unit
) {
    Box(modifier = Modifier.height(40.dp)) {
        if (firstLoaded) {
            if (isLoading) {
                if (isFollowing) {
                    Button(
                        onClick = {},
                        modifier = Modifier.width(120.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Button(onClick = {}, modifier = Modifier.width(120.dp)) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            } else {
                if (isFollowing) {
                    Button(
                        onClick = {
                            onUnFollowClick()
                        },
                        modifier = Modifier.width(120.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(text = stringResource(R.string.unfollow))
                    }
                } else {
                    Button(onClick = {
                        onFollowClick()
                    }, modifier = Modifier.width(120.dp)) {
                        Text(text = stringResource(R.string.follow))
                    }
                }
            }
        }
    }
}