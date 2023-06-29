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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leinardi.forlago.library.ui.annotation.ThemePreviews
import com.leinardi.forlago.library.ui.theme.ForlagoTheme
import com.leinardi.forlago.library.ui.theme.Spacing

@Composable
fun BottomButtonBar(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 8.dp,
    shadowElevation: Dp = 0.dp,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.x02, vertical = Spacing.x01),
            content = content,
        )
    }
}

@ThemePreviews
@Composable
private fun PreviewBottomButtonBar() {
    ForlagoTheme {
        BottomButtonBar {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Button 1")
            }
            Spacer(modifier = Modifier.size(Spacing.x01))
            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Button 2")
            }
        }
    }
}
