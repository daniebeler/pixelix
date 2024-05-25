package com.daniebeler.pfpixelix.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonRowElement(
    icon: ImageVector,
    text: String,
    smallText: String = "",
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            Modifier.padding(start = 18.dp, top = 12.dp, bottom = 12.dp),
            tint = color
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text, color = color)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp, color = color)
            }
        }
    }
}

@Composable
fun ButtonRowElement(
    @DrawableRes icon: Int,
    text: String,
    smallText: String = "",
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
            Modifier
                .padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
                .height(24.dp)
                .width(24.dp)
                .clip(
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text, color = color)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp, color = color)
            }
        }
    }
}

@Composable
fun ButtonRowElement(
    icon: ImageBitmap,
    text: String,
    smallText: String = "",
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {
        Image(
            icon,
            contentDescription = "",
            Modifier
                .padding(start = 18.dp, top = 12.dp, bottom = 12.dp)
                .height(24.dp)
                .width(24.dp)
                .clip(
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(text = text, color = color)
            if (smallText.isNotBlank()) {
                Text(text = smallText, fontSize = 12.sp, lineHeight = 6.sp, color = color)
            }
        }
    }
}

@Composable
fun SwitchRowItem(
    icon: ImageVector, text: String, isChecked: Boolean, onCheckedChange: (checked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text)
        }
        Switch(checked = isChecked, onCheckedChange = { onCheckedChange(it) })
    }
}