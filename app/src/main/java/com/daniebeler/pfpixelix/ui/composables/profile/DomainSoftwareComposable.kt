package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.DomainSoftware
import com.daniebeler.pfpixelix.ui.composables.post.CustomBottomSheetElement
import com.daniebeler.pfpixelix.utils.Share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainSoftwareComposable(domainSoftware: DomainSoftware, openUrl: (url: String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Image(
        painterResource(id = domainSoftware.icon),
        contentDescription = "",
        modifier = Modifier
            .height(24.dp)
            .clickable { showBottomSheet = true }
    )
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Row {
                    Image(
                        painterResource(id = domainSoftware.icon),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = domainSoftware.name)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = domainSoftware.description)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = domainSoftware.link,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = { openUrl(domainSoftware.link) })
                )
            }
        }
    }
}