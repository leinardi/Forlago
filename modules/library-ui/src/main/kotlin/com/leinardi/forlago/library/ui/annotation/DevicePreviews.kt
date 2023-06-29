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

package com.leinardi.forlago.library.ui.annotation

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(
    name = "1. Phone - Light theme",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "2. Phone - Dark theme",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "3. Landscape - Light theme",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "4. Landscape - Dark theme",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "5. Foldable - Light theme",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "6. Foldable - Dark theme",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
@Preview(
    name = "7. Tablet - Light theme",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFBFCFF,
)
@Preview(
    name = "8. Tablet - Dark theme",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF001E2E,
)
annotation class DevicePreviews
