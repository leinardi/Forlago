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

package com.leinardi.forlago.library.android.api.strictmode

import android.os.Build
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

// This can't be initialized using `androidx.startup.Initializer` or it will cause crashes in 3rd party libs using Content Providers
// and writing data on the main thread (e.g. LeakCanary and AndroidTestRunner).
fun configureStrictMode(detectCleartextNetwork: Boolean) {
    if (BuildConfig.DEBUG) {
        val builderThread = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .permitDiskReads()
            .permitCustomSlowCalls()
            .penaltyLog()
            .penaltyDeath()
            .detectResourceMismatches()
        StrictMode.setThreadPolicy(builderThread.build())

        val builderVM = StrictMode.VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectLeakedRegistrationObjects()
            .detectFileUriExposure()
            .penaltyLog()
            .penaltyDeath()
            .detectContentUriWithoutPermission()
            .detectCredentialProtectedWhileLocked()
            .detectImplicitDirectBoot()
            // .detectUntaggedSockets() // https://github.com/square/okhttp/issues/3537#issuecomment-974861679
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // False positive on API < 30
                    detectActivityLeaks()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    detectIncorrectContextUse()
                    // detectUnsafeIntentLaunch() // It detects deep links as unsafe
                }
                if (detectCleartextNetwork) {
                    detectCleartextNetwork()
                }
            }
        StrictMode.setVmPolicy(builderVM.build())
    }
}
