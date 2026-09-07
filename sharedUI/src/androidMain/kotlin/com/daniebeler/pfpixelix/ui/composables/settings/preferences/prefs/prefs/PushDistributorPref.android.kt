package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.daniebeler.pfpixelix.domain.model.Visibility
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic.radioButtonBlock
import com.daniebeler.pfpixelix.utils.KmpContext
import org.jetbrains.compose.resources.stringResource
import org.unifiedpush.android.connector.UnifiedPush
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.default_license
import pixelix.app.generated.resources.ok

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
actual fun PushDistributorPrefDialog(
    distributor: String,
    setDistributor: (distributor: String) -> Unit,
    onDismiss: () -> Unit,
    context: KmpContext
) {
    val distributors = UnifiedPush.getDistributors(context)

    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.align(Alignment.Center).padding(24.dp).widthIn(max = 400.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 6.dp
            ) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    item {
                        Text(
                            text = "Distributor",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "A distributor is the background app or service that delivers notifications to Pixelix.\n" +
                                    "\n" +
                                    "Choose a custom UnifiedPush provider (like ntfy or Gotify) if you have one installed, or select the default Google service.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    itemsIndexed(distributors) { index, it ->
                        SegmentedListItem(
                            content = {
                                Text(
                                    text = if (it == "com.daniebeler.pfpixelix") {
                                        "Google fallback"
                                    } else {
                                        it
                                    }, style = MaterialTheme.typography.bodyMedium
                                )
                            }, onClick = {
                                setDistributor(it)
                            }, shapes = ListItemDefaults.segmentedShapes(
                                index = index, count = distributors.size
                            ),
                            trailingContent = radioButtonBlock(it == distributor),
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    onDismiss()
                                }) { Text(stringResource(Res.string.ok)) }
                        }
                    }

                }
            }
        }
    }
}