/*
 * Copyright 2023 Roberto Leinardi.
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
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.component.placeholder.placeholder
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Composable
fun SuggestionChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    shape: Shape = SuggestionChipDefaults.shape,
    colors: ChipColors = SuggestionChipDefaults.suggestionChipColors(),
    elevation: ChipElevation? = SuggestionChipDefaults.suggestionChipElevation(),
    border: BorderStroke? = SuggestionChipDefaults.suggestionChipBorder(enabled),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: Boolean = false,
) {
    androidx.compose.material3.SuggestionChip(
        onClick = onClick,
        label = label,
        modifier = modifier.placeholder(placeholder),
        enabled = enabled,
        icon = icon,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
    )
}

@ThemePreviews
@Composable
private fun PreviewChoiceChipEnabled() {
    ForlagoTheme {
        SuggestionChip(
            onClick = {},
            label = { Text("Enabled") },
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewChoiceChipDisabled() {
    ForlagoTheme {
        SuggestionChip(
            onClick = {},
            label = { Text("Disabled") },
            enabled = false,
        )
    }
}
