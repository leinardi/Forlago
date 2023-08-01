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

package com.leinardi.forlago.library.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFFb4262d)
val md_theme_light_onPrimary = Color(0xFFffffff)
val md_theme_light_primaryContainer = Color(0xFFffdad6)
val md_theme_light_onPrimaryContainer = Color(0xFF410004)
val md_theme_light_secondary = Color(0xFF98470b)
val md_theme_light_onSecondary = Color(0xFFffffff)
val md_theme_light_secondaryContainer = Color(0xFFffdbc7)
val md_theme_light_onSecondaryContainer = Color(0xFF331100)
val md_theme_light_tertiary = Color(0xFF036e00)
val md_theme_light_onTertiary = Color(0xFFffffff)
val md_theme_light_tertiaryContainer = Color(0xFF96fa79)
val md_theme_light_onTertiaryContainer = Color(0xFF002200)
val md_theme_light_error = Color(0xFFba1b1b)
val md_theme_light_errorContainer = Color(0xFFffdad4)
val md_theme_light_onError = Color(0xFFffffff)
val md_theme_light_onErrorContainer = Color(0xFF410001)
val md_theme_light_background = Color(0xFFfafdfa)
val md_theme_light_onBackground = Color(0xFF191c1b)
val md_theme_light_surface = Color(0xFFfafdfa)
val md_theme_light_onSurface = Color(0xFF191c1b)
val md_theme_light_surfaceVariant = Color(0xFFf5dddb)
val md_theme_light_onSurfaceVariant = Color(0xFF524342)
val md_theme_light_outline = Color(0xFF857372)
val md_theme_light_inverseOnSurface = Color(0xFFeff1ef)
val md_theme_light_inverseSurface = Color(0xFF2e3130)
val md_theme_light_inversePrimary = Color(0xFFffb3ae)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFFb4262d)
val md_theme_light_outlineVariant = Color(0xFFBEC8C8)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFffb3ae)
val md_theme_dark_onPrimary = Color(0xFF69000b)
val md_theme_dark_primaryContainer = Color(0xFF910518)
val md_theme_dark_onPrimaryContainer = Color(0xFFffdad6)
val md_theme_dark_secondary = Color(0xFFffb68b)
val md_theme_dark_onSecondary = Color(0xFF542200)
val md_theme_dark_secondaryContainer = Color(0xFF773300)
val md_theme_dark_onSecondaryContainer = Color(0xFFffdbc7)
val md_theme_dark_tertiary = Color(0xFF7bdd60)
val md_theme_dark_onTertiary = Color(0xFF003a00)
val md_theme_dark_tertiaryContainer = Color(0xFF015300)
val md_theme_dark_onTertiaryContainer = Color(0xFF96fa79)
val md_theme_dark_error = Color(0xFFffb4a9)
val md_theme_dark_errorContainer = Color(0xFF930006)
val md_theme_dark_onError = Color(0xFF680003)
val md_theme_dark_onErrorContainer = Color(0xFFffdad4)
val md_theme_dark_background = Color(0xFF191c1b)
val md_theme_dark_onBackground = Color(0xFFe1e3e1)
val md_theme_dark_surface = Color(0xFF191c1b)
val md_theme_dark_onSurface = Color(0xFFe1e3e1)
val md_theme_dark_surfaceVariant = Color(0xFF524342)
val md_theme_dark_onSurfaceVariant = Color(0xFFd8c2c0)
val md_theme_dark_outline = Color(0xFF9f8c8a)
val md_theme_dark_inverseOnSurface = Color(0xFF191c1b)
val md_theme_dark_inverseSurface = Color(0xFFe1e3e1)
val md_theme_dark_inversePrimary = Color(0xFF910518)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFffb3ae)
val md_theme_dark_outlineVariant = Color(0xFF3F4949)
val md_theme_dark_scrim = Color(0xFF000000)

val ColorScheme.warning: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFFB956) else Color(0xFFFFA600)

val ColorScheme.onWarning: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF472A00) else Color(0xFFFFFFFF)

val ColorScheme.success: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF8BDC37) else Color(0xFF6BB908)

val ColorScheme.onSuccess: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1A3700) else Color(0xFFFFFFFF)
