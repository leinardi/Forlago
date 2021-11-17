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

package com.leinardi.template

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.SystemClock
import com.leinardi.template.core.feature.FeatureManager
import com.leinardi.template.core.navigation.TemplateNavigator
import com.leinardi.template.feature.account.AccountFeature
import com.leinardi.template.feature.bar.BarFeature
import com.leinardi.template.feature.debug.DebugFeature
import com.leinardi.template.feature.foo.FooFeature
import com.leinardi.template.ui.MainActivity
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Template : Application() {
    @Inject lateinit var featureManager: FeatureManager
    @Inject lateinit var navigator: TemplateNavigator

    override fun onCreate() {
        super.onCreate()
        configureStrictMode()
        registerFeatures()
        simulateHeavyLoad()
    }

    private fun configureStrictMode() {
        // This can't be initialized using `androidx.startup.Initializer` or it will cause crashes in 3rd party libs using Content Providers
        // and writing data on the main thread (e.g. LeakCanary and AndroidTestRunner).
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
                // .detectActivityLeaks() // https://issuetracker.google.com/issues/204905432
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

    @Suppress("MagicNumber")
    private fun simulateHeavyLoad() {
        SystemClock.sleep(1500)
    }

    private fun registerFeatures() {
        featureManager.register(
            listOf(
                AccountFeature(MainActivity.createIntent(this), navigator),
                FooFeature(),
                BarFeature(),
                DebugFeature(),
            ),
        )
    }
}
