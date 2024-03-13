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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.navigation.compose.rememberNavController
import com.leinardi.forlago.library.ui.theme.ForlagoTheme

@Composable
fun PreviewFeature(content: @Composable () -> Unit) {
    if (LocalInspectionMode.current) {
        ForlagoTheme {
            CompositionLocalProvider(
                LocalNavHostController provides rememberNavController(),
                LocalSnackbarHostState provides remember { SnackbarHostState() },
                LocalMainScaffoldPadding provides remember { mutableStateOf(PaddingValues()) },
            ) {
                content()
            }
        }
    } else {
        error("This Composable should be used only inside a @PreviewFeature function")
    }
}
