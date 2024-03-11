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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.preview.PreviewThemes
import com.leinardi.forlago.library.ui.preview.loremIpsum
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Composable
fun IconButtonDropdownMenu(
    icon: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    offset: DpOffset = DpOffset(0.dp, 0.dp),
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = { expanded.value = true },
            enabled = enabled,
            colors = colors,
        ) {
            icon()
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            offset = offset,
            content = content,
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewExpertListDropDownMenu() {
    ForlagoTheme {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            IconButtonDropdownMenu(
                icon = {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "",
                    )
                },
                content = {
                    DropdownMenuItem(
                        text = { Text(loremIpsum(2)) },
                        onClick = { },
                    )
                },
            )
        }
    }
}
