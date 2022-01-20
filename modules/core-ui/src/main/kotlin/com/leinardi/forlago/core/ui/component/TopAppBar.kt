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

package com.leinardi.forlago.core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.core.ui.R
import com.leinardi.forlago.core.ui.theme.ForlagoTheme

@Composable
fun TopAppBar(
    title: String = stringResource(R.string.i18n_app_name),
    navigateUp: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    androidx.compose.material.TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = title,
            )
        },
        navigationIcon = if (navigateUp != null) {
            {
                IconButton(
                    onClick = navigateUp,
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.i18n_navigate_up),
                    )
                }
            }
        } else {
            null
        },
        actions = actions,
    )
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarPreview() {
    ForlagoTheme {
        TopAppBar()
    }
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithNavigationIcon() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
        ) { }
    }
}

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithAction() {
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

@Composable
@Preview(showBackground = false)
fun SimpleAppBarWithNavigationIconAndAction() {
    ForlagoTheme {
        TopAppBar(
            title = "Page title",
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "")
                }
            },
            navigateUp = { },
        )
    }
}
