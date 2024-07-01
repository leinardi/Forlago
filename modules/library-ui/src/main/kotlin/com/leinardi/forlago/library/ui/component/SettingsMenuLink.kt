/*
 * Copyright 2024 Roberto Leinardi.
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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.preview.PreviewThemes
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun SettingsMenuLink(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
    ) {
        val alpha = if (enabled) 1f else ContentAlpha.disabled
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .clickable(enabled = enabled, onClick = onClick)
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
            if (action != null) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = Spacing.half)
                        .height(56.dp),
                )
                SettingsTileAction {
                    action.invoke()
                }
            }
        }
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLink() {
    ForlagoTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkSubtitle() {
    ForlagoTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkAction() {
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    ForlagoTheme {
        SettingsMenuLink(
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
            action = {
                Checkbox(
                    checked = rememberCheckBoxState,
                    onCheckedChange = { newState ->
                        rememberCheckBoxState = newState
                    },
                )
            },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkIcon() {
    ForlagoTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuLinkIconAction() {
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    ForlagoTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
            action = {
                Checkbox(
                    checked = rememberCheckBoxState,
                    onCheckedChange = { newState ->
                        rememberCheckBoxState = newState
                    },
                )
            },
        )
    }
}
