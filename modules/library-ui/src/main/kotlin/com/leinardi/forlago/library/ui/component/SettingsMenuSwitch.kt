/*
 * Copyright 2023 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.library.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun SettingsMenuSwitch(
    title: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
) {
    Surface(modifier = modifier.toggleable(enabled = enabled, value = checked, onValueChange = onCheckedChange)) {
        val alpha = if (enabled) 1f else ContentAlpha.disabled
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .padding(vertical = Spacing.x01)
                .alpha(alpha),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                SettingsTileIcon(icon = icon)
            } else {
                Spacer(modifier = Modifier.size(Spacing.x02))
            }
            SettingsTileTexts(title = title, subtitle = subtitle)
            SettingsTileAction {
                Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.padding(end = Spacing.x01))
            }
        }
    }
}

@ThemePreviews
@Composable
private fun PreviewSettingsMenuSwitchOn() {
    ForlagoTheme {
        SettingsMenuSwitch(
            checked = true,
            onCheckedChange = {},
            title = { Text(text = "Hello") },
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewSettingsMenuSwitchOff() {
    ForlagoTheme {
        SettingsMenuSwitch(
            checked = false,
            onCheckedChange = {},
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}
