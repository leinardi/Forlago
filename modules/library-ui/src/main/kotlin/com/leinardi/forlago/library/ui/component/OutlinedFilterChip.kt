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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.leinardi.forlago.library.ui.ext.default
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@ExperimentalMaterialApi
@Composable
fun OutlinedFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    border: BorderStroke? = ChipDefaults.outlinedBorder,
    colors: SelectableChipColors = ChipDefaults.outlinedFilterChipColors(),
    placeholder: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    selectedIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier.placeholder(visible = placeholder, highlight = PlaceholderHighlight.default()),
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        colors = colors,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        selectedIcon = selectedIcon,
        trailingIcon = trailingIcon,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedFilterChipEnabled() {
    ForlagoTheme {
        OutlinedFilterChip(
            selected = false,
            onClick = {},
        ) {
            Text("Enabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedFilterChipDisabled() {
    ForlagoTheme {
        OutlinedFilterChip(
            selected = false,
            onClick = {},
            enabled = false,
        ) {
            Text("Disabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedFilterChipSelected() {
    ForlagoTheme {
        OutlinedFilterChip(
            selected = true,
            onClick = {},
        ) {
            Text("Selected")
        }
    }
}
