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

package com.leinardi.forlago.library.ui.api

import androidx.appcompat.app.AppCompatDelegate

enum class NightMode(@AppCompatDelegate.NightMode val intValue: Int) {
    DEFAULT(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    NO(AppCompatDelegate.MODE_NIGHT_NO),
    YES(AppCompatDelegate.MODE_NIGHT_YES);

    companion object {
        fun fromIntValue(@AppCompatDelegate.NightMode value: Int) = values().first { it.intValue == value }
    }
}
