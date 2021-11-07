/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leinardi.template.ui.theme.TemplateTheme

@Composable
fun SettingsMenuLink(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(modifier = modifier) {
        val alpha = if (enabled) 1f else ContentAlpha.disabled
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 64.dp)
                .clickable(enabled = enabled) { onClick() }
                .alpha(alpha),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                SettingsTileIcon(icon = icon)
            } else {
                Spacer(modifier = Modifier.size(16.dp))
            }
            SettingsTileTexts(title = title, subtitle = subtitle)
        }
        if (action != null) {
            Divider(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(56.dp)
                    .width(1.dp),
            )
            SettingsTileAction {
                action.invoke()
            }
        }
    }
}

@Preview
@Composable
internal fun SettingsMenuLinkPreview() {
    TemplateTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        ) { }
    }
}

@Preview
@Composable
internal fun SettingsMenuLinkActionPreview() {
    var rememberCheckBoxState by remember { mutableStateOf(true) }
    TemplateTheme {
        SettingsMenuLink(
            icon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
            action = {
                Checkbox(checked = rememberCheckBoxState, onCheckedChange = { newState ->
                    rememberCheckBoxState = newState
                })
            },
        ) { }
    }
}
