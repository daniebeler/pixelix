package com.daniebeler.pfpixelix.ui.composables.settings.preferences.basic

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daniebeler.pfpixelix.utils.rememberPrefBoolState
import com.daniebeler.pfpixelix.utils.rememberPrefIntState

@Composable
fun SwitchPref(
    key: String,
    @DrawableRes leadingIcon: Int,
    title: String,
    desc: String? = null,
    default: Boolean = false,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    var boolState by rememberPrefBoolState(key, default)
    SettingPref(
        leadingIcon = leadingIcon,
        title = title,
        desc = desc,
        trailingContent = {
            Box(modifier = Modifier.padding(end = 14.dp)) {
                Switch(checked = boolState, onCheckedChange = {
                    boolState = it
                    onCheckedChange(it)
                })
            }
        },
        onClick = {
            boolState = !boolState
            onCheckedChange(boolState)
        }
    )
}

@Composable
fun SwitchIntPref(
    key: String,
    @DrawableRes leadingIcon: Int,
    title: String,
    desc: String? = null,
    default: Int = SettingPrefUtil.OFF,
    onCheckedChange: (value: Int) -> Unit
) {
    var intState by rememberPrefIntState(key, default)
    SettingPref(
        leadingIcon = leadingIcon,
        title = title,
        desc = desc,
        trailingContent = {
            Box(modifier = Modifier.padding(end = 14.dp)) {
                Switch(checked = intState == SettingPrefUtil.ON, onCheckedChange = {
                    intState = if (it) SettingPrefUtil.ON else SettingPrefUtil.OFF
                    onCheckedChange(intState)
                })
            }
        },
        onClick = {
            intState = if (intState == SettingPrefUtil.ON) {
                SettingPrefUtil.OFF
            } else {
                SettingPrefUtil.ON
            }
            onCheckedChange(intState)
        }
    )
}