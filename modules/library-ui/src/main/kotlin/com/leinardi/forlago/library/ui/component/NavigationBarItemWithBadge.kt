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

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.leinardi.forlago.library.ui.preview.PreviewThemes
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun RowScope.NavigationBarItemWithBadge(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    label: String? = null,
    badgeCount: Int? = null,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
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
        modifier = modifier,
        label = label?.let {
            {
                Text(
                    text = label,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
            }
        },
        colors = colors,
    )
}

@Composable
fun RowScope.MainNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    label: String? = null,
    badgeCount: Int? = null,
    colors: NavigationBarItemColors = LocalMainNavigationBarItemColors.current,
) {
    NavigationBarItemWithBadge(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        label = label,
        badgeCount = badgeCount,
        colors = colors,
    )
}

val LocalMainNavigationBarItemColors: ProvidableCompositionLocal<NavigationBarItemColors> =
    compositionLocalOf { error("No NavigationBarItemColors provided") }

@PreviewThemes
@Composable
private fun PreviewNavigationBarItemWithBadgeSelected() {
    ForlagoTheme {
        NavigationBar {
            NavigationBarItemWithBadge(
                selected = true,
                onClick = {},
                icon = Icons.Default.Inbox,
                label = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_app_name),
                badgeCount = 12,
            )
        }
    }
}

@PreviewThemes
@Composable
private fun PreviewNavigationBarItemWithBadgeUnselected() {
    ForlagoTheme {
        NavigationBar {
            NavigationBarItemWithBadge(
                selected = false,
                onClick = {},
                icon = Icons.Default.Inbox,
                label = stringResource(com.leinardi.forlago.library.i18n.R.string.i18n_app_name),
                badgeCount = 12,
            )
        }
    }
}
