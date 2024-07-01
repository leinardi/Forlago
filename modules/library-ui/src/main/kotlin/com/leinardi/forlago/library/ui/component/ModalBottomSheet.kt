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

@file:Suppress("Material2")

package com.leinardi.forlago.library.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable () -> Unit,
) {
    // Migrate to Material 3: https://issuetracker.google.com/issues/328949006
    androidx.compose.material.navigation.ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        modifier = modifier,
        sheetShape = shape,
        sheetElevation = sheetElevation,
        sheetBackgroundColor = containerColor,
        sheetContentColor = contentColor,
        scrimColor = scrimColor,
        content = {
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = shape,
                color = containerColor,
                contentColor = contentColor,
                tonalElevation = tonalElevation,
            ) {
                content()
            }
        },
    )
}
