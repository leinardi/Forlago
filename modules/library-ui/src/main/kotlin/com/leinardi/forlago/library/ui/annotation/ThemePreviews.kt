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

package com.leinardi.forlago.library.ui.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents light and dark themes. Add this annotation to a
 * composable to render both themes.
 */
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
annotation class ThemePreviews
