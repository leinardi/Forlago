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

package com.leinardi.template.initializer

import android.content.Context
import android.os.Build
import android.os.StrictMode
import androidx.startup.Initializer
import com.leinardi.template.BuildConfig

class StrictModeInitializer : Initializer<Unit> {

    override fun create(context: Context) {
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
//                .detectActivityLeaks() // https://issuetracker.google.com/issues/204905432
                .detectLeakedSqlLiteObjects()
                .detectLeakedRegistrationObjects()
                .detectFileUriExposure()
                .detectCleartextNetwork()
                .penaltyLog()
                .penaltyDeath()
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detectContentUriWithoutPermission()
                        detectUntaggedSockets()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        detectCredentialProtectedWhileLocked()
                        detectImplicitDirectBoot()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        detectIncorrectContextUse()
                        detectUnsafeIntentLaunch()
                    }
                }

            StrictMode.setVmPolicy(builderVM.build())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
