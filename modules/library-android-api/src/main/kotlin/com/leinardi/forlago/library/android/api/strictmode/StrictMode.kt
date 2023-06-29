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

package com.leinardi.forlago.library.android.api.strictmode

import android.os.StrictMode
import com.leinardi.forlago.library.android.api.BuildConfig

fun <T> noStrictMode(disableVm: Boolean = true, disableThread: Boolean = true, block: () -> T): T {
    val vmPolicy = StrictMode.getVmPolicy()
    val threadPolicy = StrictMode.getThreadPolicy()
    if (disableVm && BuildConfig.DEBUG) {
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
    }
    if (disableThread && BuildConfig.DEBUG) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().build())
    }
    return block().also {
        if (disableVm && BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(vmPolicy)
        }
        if (disableThread && BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(threadPolicy)
        }
    }
}
