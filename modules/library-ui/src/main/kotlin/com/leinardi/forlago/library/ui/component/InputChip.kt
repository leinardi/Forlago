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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SelectableChipColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@ExperimentalMaterialApi
@Composable
fun InputChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRemoveClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    border: BorderStroke? = null,
    colors: SelectableChipColors = ChipDefaults.filterChipColors(),
    placeholder: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    selectedIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = border,
        colors = colors,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        selectedIcon = selectedIcon,
        trailingIcon = if (onRemoveClick != null) {
            {
                val trailingIconColor = colors.leadingIconColor(enabled, selected)
                CompositionLocalProvider(
                    LocalContentColor provides trailingIconColor.value,
                    LocalContentAlpha provides trailingIconColor.value.alpha,
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                onClick = onRemoveClick,
                                enabled = enabled,
                                role = Role.Button,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false, radius = 16.dp),
                            ),
                    )
                }
            }
        } else {
            null
        },
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewInputChipEnabled() {
    ForlagoTheme {
        InputChip(
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
fun PreviewInputChipDisabled() {
    ForlagoTheme {
        InputChip(
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
fun PreviewInputChipSelected() {
    ForlagoTheme {
        InputChip(
            selected = true,
            onClick = {},
            onRemoveClick = {},
        ) {
            Text("Selected")
        }
    }
}
