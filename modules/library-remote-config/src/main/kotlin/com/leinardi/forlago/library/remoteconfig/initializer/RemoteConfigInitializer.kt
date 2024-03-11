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

package com.leinardi.forlago.library.remoteconfig.initializer

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.leinardi.forlago.library.logging.api.initializer.TimberInitializer
import com.leinardi.forlago.library.remoteconfig.BuildConfig
import com.leinardi.forlago.library.remoteconfig.getRemoteConfigDefaults
import timber.log.Timber

class RemoteConfigInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (FirebaseApp.getApps(context).isNotEmpty()) {
            val configSettings = if (BuildConfig.DEBUG) remoteConfigSettings { minimumFetchIntervalInSeconds = 0 } else remoteConfigSettings { }
            FirebaseRemoteConfig.getInstance(Firebase.app).apply {
                setConfigSettingsAsync(configSettings)
                setDefaultsAsync(getRemoteConfigDefaults(context))
                Timber.d("Fetching remote config...")
                fetchAndActivate()
                    .addOnFailureListener { Timber.e(it) }
                    .addOnSuccessListener { Timber.d("Remote config fetched successfully") }
            }
        } else {
            Timber.w("Firebase doesn't seem to be initialized. Is the google-services.json missing from the app module?")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(TimberInitializer::class.java)
}
