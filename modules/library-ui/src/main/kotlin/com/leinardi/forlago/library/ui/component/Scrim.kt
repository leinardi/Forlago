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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Composable
fun Scrim(
    modifier: Modifier = Modifier,
    startColor: Color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f),
    endColor: Color = Color.Transparent,
    minHeight: Dp = 96.dp,
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = minHeight)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(startColor, endColor),
                ),
            )
            .then(modifier),
    )
}

@ThemePreviews
@Composable
private fun PreviewScrim() {
    ForlagoTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
        ) {
            Scrim(minHeight = 48.dp)
        }
    }
}
