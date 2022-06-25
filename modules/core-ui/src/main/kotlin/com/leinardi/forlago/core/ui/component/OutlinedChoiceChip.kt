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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import com.leinardi.forlago.core.ui.theme.ForlagoTheme

@ExperimentalMaterialApi
@Composable
fun OutlinedChoiceChip(
    activated: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    border: BorderStroke? = if (activated) {
        BorderStroke(ChipDefaults.OutlinedBorderSize, MaterialTheme.colors.primary.copy(alpha = ChipDefaults.OutlinedBorderOpacity))
    } else {
        ChipDefaults.outlinedBorder
    },
    colors: SelectableChipColors = ChipDefaults.outlinedFilterChipColors(
        selectedBackgroundColor = MaterialTheme.colors.primary.copy(
            alpha = 0.16f,
        ).compositeOver(MaterialTheme.colors.surface),
        selectedContentColor = MaterialTheme.colors.primary.copy(
            alpha = 0.16f,
        ).compositeOver(MaterialTheme.colors.primary.copy(ChipDefaults.ContentOpacity)),
        selectedLeadingIconColor = MaterialTheme.colors.primary.copy(
            alpha = 0.16f,
        ).compositeOver(MaterialTheme.colors.primary.copy(ChipDefaults.ContentOpacity).copy(ChipDefaults.LeadingIconOpacity)),
    ),
    placeholder: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    selectedIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    ChoiceChip(
        activated = activated,
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
        trailingIcon = trailingIcon,
        content = content,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedChoiceChipEnabled() {
    ForlagoTheme {
        OutlinedChoiceChip(
            activated = false,
            onClick = {},
        ) {
            Text("Enabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedChoiceChipDisabled() {
    ForlagoTheme {
        OutlinedChoiceChip(
            activated = false,
            onClick = {},
            enabled = false,
        ) {
            Text("Disabled")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedChoiceChipActivated() {
    ForlagoTheme {
        OutlinedChoiceChip(
            activated = true,
            onClick = {},
        ) {
            Text("Activated")
        }
    }
}
