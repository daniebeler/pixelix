package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.DomainSoftware

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainSoftwareComposable(domainSoftware: DomainSoftware, openUrl: (url: String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Image(painterResource(id = domainSoftware.icon),
        contentDescription = "",
        modifier = Modifier
            .height(24.dp)
            .clickable { showBottomSheet = true })

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painterResource(id = domainSoftware.icon),
                        contentDescription = null,
                        modifier = Modifier.height(56.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = domainSoftware.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = domainSoftware.description)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { openUrl(domainSoftware.link) }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Text(text = stringResource(R.string.more_information))
                }
            }
        }
    }
}