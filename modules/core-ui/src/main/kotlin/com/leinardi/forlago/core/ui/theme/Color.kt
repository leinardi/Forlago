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

package com.leinardi.forlago.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ForlagoLightColors = lightColors(
    primary = Color(0xFFb4262d),
    primaryVariant = Color(0xFF410004),
    secondary = Color(0xFF98470b),
    secondaryVariant = Color(0xFF331100),
    background = Color(0xFFfafdfa),
    surface = Color(0xFFfafdfa),
    error = Color(0xFFba1b1b),
    onPrimary = Color(0xFFffffff),
    onSecondary = Color(0xFFffffff),
    onBackground = Color(0xFF191c1b),
    onSurface = Color(0xFF191c1b),
    onError = Color(0xFFffffff),
)
val ForlagoDarkColors = darkColors(
    primary = Color(0xFFffb3ae),
    primaryVariant = Color(0xFFffdad6),
    secondary = Color(0xFFffb68b),
    secondaryVariant = Color(0xFFffdbc7),
    background = Color(0xFF191c1b),
    surface = Color(0xFF191c1b),
    error = Color(0xFFffb4a9),
    onPrimary = Color(0xFF69000b),
    onSecondary = Color(0xFF542200),
    onBackground = Color(0xFFe1e3e1),
    onSurface = Color(0xFFe1e3e1),
    onError = Color(0xFF680003),
)

val Colors.textPrimary: Color
    @Composable
    get() = MaterialTheme.colors.onSurface.copy(alpha = 0.87f)

val Colors.textSecondary: Color
    @Composable
    get() = MaterialTheme.colors.onSurface.copy(alpha = 0.60f)
