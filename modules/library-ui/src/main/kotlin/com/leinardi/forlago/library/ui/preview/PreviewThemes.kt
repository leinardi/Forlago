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

package com.leinardi.forlago.library.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

/**
 * A MultiPreview annotation for desplaying a @[Composable] method using light and dark themes.
 *
 * Note that the app theme should support dark and light modes for these previews to be different.
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "1. Light theme",
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "2. Dark theme",
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "3. Red",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
)
@Preview(
    name = "4. Blue",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
)
@Preview(
    name = "5. Green",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
)
@Preview(
    name = "6. Yellow",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
)
annotation class PreviewThemes
