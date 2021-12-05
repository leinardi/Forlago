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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onNavigateUp: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    contextual: Boolean = false,
    contextualTitle: String = "",
    contextualSubtitle: String = "",
    contextualColors: TopAppBarColors = TopAppBarDefaults.contextualTopAppBarColors(),
    contextualActions: @Composable RowScope.() -> Unit = {},

    ) {
    androidx.compose.material3.TopAppBar(
        title = {
            Column {
                Text(
                    text = if (contextual) contextualTitle else title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                if (!subtitle.isNullOrEmpty()) {
                    Text(
                        text = if (contextual) contextualSubtitle else subtitle,
                        modifier = Modifier.alpha(ContentAlpha.medium),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = onNavigateUp?.let {
            {
                IconButton(
                    onClick = it,
                ) {
                    Icon(
                        imageVector = if (contextual) {
                            Icons.Default.Clear
                        } else {
                            Icons.Default.ArrowBack
                        },
                        contentDescription = null,
                    )
                }
            }
        } ?: {},
        actions = if (contextual) contextualActions else actions,
        windowInsets = windowInsets,
        colors = if (contextual) contextualColors else colors,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun TopAppBarDefaults.contextualTopAppBarColors(): TopAppBarColors =
    TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

@ThemePreviews
@Composable
private fun PreviewTopAppBarWithNavigationIcon() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewTopAppBarWithSubtitle() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
            subtitle = "Page subtitle",
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewTopAppBarWithAction() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "")
                }
            },
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewTopAppBarWithNavigationIconAndAction() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "")
                }
            },
            onNavigateUp = { },
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewTopAppBarWithContextualModeAndAction() {
    ForlagoTheme {
        TopAppBar(
            title = "",
            onNavigateUp = { },
            contextualTitle = "Contextual title",
            contextualActions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "")
                }
            },
            contextual = true,
        )
    }
}
