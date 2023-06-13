/*
 * Copyright 2022 Roberto Leinardi.
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

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.leinardi.forlago.library.ui.R
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun RowScope.MainNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    label: String? = null,
    badgeCount: Int? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            BadgedBox(
                badge = {
                    badgeCount?.let { count ->
                        Badge(
                            modifier = Modifier.padding(top = Spacing.x01),
                        ) {
                            Text(count.toString())
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                )
            }
        },
        label = label?.let { { Text(label) } },
        modifier = modifier,
    )
}

@ThemePreviews
@Composable
private fun PreviewMainNavigationBarSelected() {
    ForlagoTheme {
        NavigationBar {
            MainNavigationBarItem(
                selected = true,
                onClick = {},
                icon = Icons.Default.Inbox,
                label = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_app_name),
                badgeCount = 12,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PreviewMainNavigationBarUnselected() {
    ForlagoTheme {
        NavigationBar {
            MainNavigationBarItem(
                selected = false,
                onClick = {},
                icon = Icons.Default.Inbox,
                label = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_app_name),
                badgeCount = 12,
            )
        }
    }
}
