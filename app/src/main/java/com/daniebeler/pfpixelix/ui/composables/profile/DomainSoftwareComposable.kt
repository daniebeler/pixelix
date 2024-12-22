package com.daniebeler.pfpixelix.ui.composables.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.R
import com.daniebeler.pfpixelix.domain.model.DomainSoftware
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainSoftwareComposable(domainSoftware: DomainSoftware, openUrl: (url: String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
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

                TextButton (
                    onClick = { openUrl(domainSoftware.link) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Visit " + domainSoftware.link)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (domainSoftware.postsCount != -1 || domainSoftware.totalUserCount != -1 || domainSoftware.activeUserCount != -1) {
                    HorizontalDivider(Modifier.padding(vertical = 12.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = domainSoftware.domain,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (domainSoftware.nodeDescription.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(domainSoftware.nodeDescription)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (domainSoftware.postsCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text("Total posts:")
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY, "%,d", domainSoftware.postsCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (domainSoftware.totalUserCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text("Total users:")
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY, "%,d", domainSoftware.totalUserCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (domainSoftware.activeUserCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text("Active users:")
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY, "%,d", domainSoftware.activeUserCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton (
                        onClick = { openUrl("https://" + domainSoftware.domain) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Visit https://" + domainSoftware.domain)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}