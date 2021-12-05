/*
 * Copyright 2021 Roberto Leinardi.
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

import android.os.Build
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val ForlagoLightColorsScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
)

private val ForlagoDarkColorsScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
)

private val ForlagoM2LightColorsScheme = lightColors(
    primary = ForlagoLightColorsScheme.primary,
    primaryVariant = ForlagoLightColorsScheme.primary,
    secondary = ForlagoLightColorsScheme.secondary,
    secondaryVariant = ForlagoLightColorsScheme.secondary,
    background = ForlagoLightColorsScheme.background,
    surface = ForlagoLightColorsScheme.surface,
    error = ForlagoLightColorsScheme.error,
    onPrimary = ForlagoLightColorsScheme.onPrimary,
    onSecondary = ForlagoLightColorsScheme.onSecondary,
    onBackground = ForlagoLightColorsScheme.onBackground,
    onSurface = ForlagoLightColorsScheme.onSurface,
    onError = ForlagoLightColorsScheme.onError,
)

private val ForlagoM2DarkColorsScheme = darkColors(
    primary = ForlagoDarkColorsScheme.primary,
    primaryVariant = ForlagoDarkColorsScheme.primary,
    secondary = ForlagoDarkColorsScheme.secondary,
    secondaryVariant = ForlagoDarkColorsScheme.secondary,
    background = ForlagoDarkColorsScheme.background,
    surface = ForlagoDarkColorsScheme.surface,
    error = ForlagoDarkColorsScheme.error,
    onPrimary = ForlagoDarkColorsScheme.onPrimary,
    onSecondary = ForlagoDarkColorsScheme.onSecondary,
    onBackground = ForlagoDarkColorsScheme.onBackground,
    onSurface = ForlagoDarkColorsScheme.onSurface,
    onError = ForlagoDarkColorsScheme.onError,
)

@Composable
fun ForlagoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val myColorScheme = when {
        dynamicColor && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        isDarkTheme -> ForlagoDarkColorsScheme
        else -> ForlagoLightColorsScheme
    }

    MaterialTheme(
        colorScheme = myColorScheme,
        typography = ForlagoTypography,
    ) {
        // (M3): MaterialTheme doesn't provide LocalIndication, remove when it does
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalIndication provides rippleIndication,
            content = {
                // (M3): Several components are not available for M3 yet, so we need to still relay on the M2 implementation
                androidx.compose.material.MaterialTheme(
                    colors = if (isDarkTheme) ForlagoM2DarkColorsScheme else ForlagoM2LightColorsScheme,
                    typography = ForlagoM2Typography,
                    content = content,
                )
            }
        )
    }
}
