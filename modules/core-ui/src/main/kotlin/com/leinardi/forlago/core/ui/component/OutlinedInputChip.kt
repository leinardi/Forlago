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
import com.leinardi.forlago.core.ui.theme.ForlagoTheme

@ExperimentalMaterialApi
@Composable
fun OutlinedInputChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRemoveClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    border: BorderStroke? = ChipDefaults.outlinedBorder,
    colors: SelectableChipColors = ChipDefaults.outlinedFilterChipColors(),
    placeholder: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    selectedIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    InputChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        onRemoveClick = onRemoveClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        colors = colors,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        selectedIcon = selectedIcon,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedInputChipEnabled() {
    ForlagoTheme {
        OutlinedInputChip(
            selected = false,
            onClick = {},
            onRemoveClick = {},
        ) {
            Text("Enabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedInputChipDisabled() {
    ForlagoTheme {
        OutlinedInputChip(
            selected = false,
            onClick = {},
            onRemoveClick = {},
            enabled = false,
        ) {
            Text("Disabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedInputChipSelected() {
    ForlagoTheme {
        OutlinedInputChip(
            selected = true,
            onClick = {},
            onRemoveClick = {},
        ) {
            Text("Selected")
        }
    }
}
