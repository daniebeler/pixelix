package com.daniebeler.pfpixelix.ui.composables.profile.server_stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.pfpixelix.di.injectViewModel
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainSoftwareComposable(
    domain: String, viewModel: ServerStatsViewModel = injectViewModel(key = "serverstats$domain") { serverStatsViewModel }
) {

    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.getData(domain)
    }

    if (viewModel.statsState.fediSoftware?.icon != null) {
        Image(painterResource(viewModel.statsState.fediSoftware!!.icon!!),
            contentDescription = viewModel.statsState.fediSoftware!!.name,
            modifier = Modifier
                .height(24.dp)
                .clickable { showBottomSheet = true })
    } else if (viewModel.statsState.isLoading) {
        CircularProgressIndicator(Modifier.size(18.dp))
    }


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
                    .verticalScroll(state = rememberScrollState())
            ) {
                if (viewModel.statsState.fediSoftware != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painterResource(viewModel.statsState.fediSoftware!!.icon!!),
                            contentDescription = null,
                            modifier = Modifier.height(56.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = viewModel.statsState.fediSoftware!!.name,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (viewModel.statsState.fediSoftware!!.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = viewModel.statsState.fediSoftware!!.description)
                    }

                    if (viewModel.statsState.fediSoftware!!.instanceCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text(stringResource(Res.string.instances))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.statsState.fediSoftware!!.instanceCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (viewModel.statsState.fediSoftware!!.statusCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text(stringResource(Res.string.total_posts))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.statsState.fediSoftware!!.statusCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (viewModel.statsState.fediSoftware!!.userCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text(stringResource(Res.string.total_users))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.statsState.fediSoftware!!.userCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (viewModel.statsState.fediSoftware!!.activeUserCount != -1) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row {
                            Text(stringResource(Res.string.active_users))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.GERMANY,
                                    "%,d",
                                    viewModel.statsState.fediSoftware!!.activeUserCount
                                ), fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    if (viewModel.statsState.fediSoftware!!.website.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                viewModel.openUrl(
                                    viewModel.statsState.fediSoftware!!.website, context
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(
                                    Res.string.visit_url, viewModel.statsState.fediSoftware!!.website
                                )
                            )
                        }
                    }

                }

                if (viewModel.statsState.fediServer != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(Modifier.padding(vertical = 12.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = viewModel.statsState.fediServer!!.domain,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (viewModel.statsState.fediServer!!.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(viewModel.statsState.fediServer!!.description)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Row {
                        Text(
                            stringResource(
                                Res.string.server_version,
                                viewModel.statsState.fediServer!!.software.name,
                                viewModel.statsState.fediServer!!.software.version
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (viewModel.statsState.fediServer!!.location.city != null || viewModel.statsState.fediServer!!.location.country != null) {
                        Row {
                            Text(
                                "Server location:"
                            )
                            Spacer(Modifier.width(8.dp))

                            if (viewModel.statsState.fediServer!!.location.city != null && viewModel.statsState.fediServer!!.location.country != null) {
                                Text(
                                    text = viewModel.statsState.fediServer!!.location.city.toString() + ", " + viewModel.statsState.fediServer!!.location.country.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            } else if (viewModel.statsState.fediServer!!.location.country != null) {
                                Text(
                                    text = viewModel.statsState.fediServer!!.location.country.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = viewModel.statsState.fediServer!!.location.city.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    if (viewModel.statsState.fediServer!!.openRegistration != null) {
                        Row {
                            Text(
                                "Open registration:"
                            )
                            Spacer(Modifier.width(8.dp))

                            if (viewModel.statsState.fediServer!!.openRegistration!!) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    tint = Color.Green,
                                    contentDescription = "true",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = Color.Red,
                                    contentDescription = "false",
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }


                    Row {
                        Text(stringResource(Res.string.total_posts))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = String.format(
                                Locale.GERMANY,
                                "%,d",
                                viewModel.statsState.fediServer!!.stats.statusCount
                            ), fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {
                        Text(stringResource(Res.string.total_users))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = String.format(
                                Locale.GERMANY,
                                "%,d",
                                viewModel.statsState.fediServer!!.stats.userCount
                            ), fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {
                        Text(stringResource(Res.string.active_users))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = String.format(
                                Locale.GERMANY,
                                "%,d",
                                viewModel.statsState.fediServer!!.stats.monthlyActiveUsers
                            ), fontWeight = FontWeight.Bold
                        )
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            viewModel.openUrl(
                                "https://" + viewModel.statsState.fediServer!!.domain, context
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.visit_url,
                                ("https://" + viewModel.statsState.fediServer!!.domain)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}