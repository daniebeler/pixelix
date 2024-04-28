package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.TableRows
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.R

@Composable
fun SwitchViewComposable(
    postsCount: Int, viewType: ViewEnum, onViewChange: (type: ViewEnum) -> Unit
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        HorizontalDivider(Modifier.padding(bottom = 12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = postsCount.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = " " + stringResource(id = R.string.posts), fontSize = 12.sp)
            }

            Row {
                Box(modifier = Modifier
                    .padding(4.dp)
                    .clickable { onViewChange(ViewEnum.Grid) }
                    .alpha(
                        if (viewType == ViewEnum.Timeline) {
                            0.5f
                        } else {
                            1f
                        }
                    )) {
                    Icon(
                        imageVector = Icons.Outlined.GridView, contentDescription = "grid view"
                    )
                }
                Box(modifier = Modifier
                    .padding(4.dp)
                    .clickable { onViewChange(ViewEnum.Timeline) }
                    .alpha(
                        if (viewType == ViewEnum.Grid) {
                            0.5f
                        } else {
                            1f
                        }
                    )) {
                    Icon(
                        imageVector = Icons.Outlined.TableRows, contentDescription = "timeline view"
                    )
                }
            }
        }
    }
}