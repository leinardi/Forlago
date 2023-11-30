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

// Taken from https://github.com/chrisbanes/tivi/blob/main/common-ui-compose/src/main/java/app/tivi/common/compose/ui/AutoSizedCircularProgressIndicator.kt
package com.leinardi.forlago.library.ui.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.leinardi.forlago.library.ui.preview.PreviewThemes
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun AutoSizedCircularProgressIndicator(
    @FloatRange(from = 0.0, to = 1.0)
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    BoxWithConstraints(modifier) {
        val diameter = with(LocalDensity.current) {
            // We need to minus the padding added within CircularProgressWithBackgroundIndicator
            min(constraints.maxWidth.toDp(), constraints.maxHeight.toDp()) - InternalPadding
        }

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(diameter + InternalPadding),
            color = color,
            strokeWidth = (diameter * StrokeDiameterFraction).coerceAtLeast(1.dp),
        )
    }
}

@Composable
fun AutoSizedCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    BoxWithConstraints(modifier) {
        val diameter = with(LocalDensity.current) {
            // We need to minus the padding added within CircularProgressWithBackgroundIndicator
            min(constraints.maxWidth.toDp(), constraints.maxHeight.toDp()) - InternalPadding
        }

        CircularProgressIndicator(
            strokeWidth = (diameter * StrokeDiameterFraction).coerceAtLeast(1.dp),
            color = color,
        )
    }
}

// Default stroke size
private val DefaultStrokeWidth = 4.dp

// Preferred diameter for CircularProgressWithBackgroundIndicator
private val DefaultDiameter = 40.dp

// Internal padding added by CircularProgressWithBackgroundIndicator
private val InternalPadding = Spacing.half

private val StrokeDiameterFraction = DefaultStrokeWidth / DefaultDiameter

@PreviewThemes
@Composable
private fun PreviewAutoSizedCircularProgressIndicator() {
    Column {
        AutoSizedCircularProgressIndicator(
            modifier = Modifier.size(16.dp),
        )

        AutoSizedCircularProgressIndicator(
            modifier = Modifier.size(24.dp),
        )

        AutoSizedCircularProgressIndicator(
            modifier = Modifier.size(48.dp),
        )

        AutoSizedCircularProgressIndicator(
            modifier = Modifier.size(64.dp),
        )

        AutoSizedCircularProgressIndicator(
            modifier = Modifier.size(128.dp),
        )
    }
}
