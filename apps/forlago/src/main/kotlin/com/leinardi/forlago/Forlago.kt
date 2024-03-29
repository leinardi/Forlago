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

package com.leinardi.forlago

import android.app.Application
import android.os.SystemClock
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.leinardi.forlago.library.android.api.strictmode.configureStrictMode
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.feature.FeatureManager
import com.leinardi.forlago.library.navigation.api.destination.NavigationDestination
import com.leinardi.forlago.library.network.api.interactor.ReadCertificatePinningEnabledInteractor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class Forlago : Application(), ImageLoaderFactory {
    @Inject lateinit var featureManager: FeatureManager

    @Inject lateinit var featureSet: Set<@JvmSuppressWildcards Feature>

    @Inject lateinit var okHttpClient: OkHttpClient

    @Inject lateinit var readCertificatePinningEnabledInteractor: ReadCertificatePinningEnabledInteractor

    override fun onCreate() {
        super.onCreate()
        NavigationDestination.DEEP_LINK_SCHEME = BuildConfig.DEEP_LINK_SCHEME
        configureStrictMode(runBlocking { readCertificatePinningEnabledInteractor() })
        registerFeatures()
        simulateHeavyLoad()
    }

    @Suppress("MagicNumber")
    private fun simulateHeavyLoad() {
        SystemClock.sleep(1500)
    }

    private fun registerFeatures() {
        featureManager.register(featureSet.toList())
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(COIL_MEMORY_CACHE_SIZE_PERCENT)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(filesDir.resolve(COIL_DISK_CACHE_DIRECTORY_NAME))
                .maxSizeBytes(COIL_DISK_CACHE_SIZE)
                .build()
        }
        .okHttpClient { okHttpClient }
        .crossfade(true)
        // Ignore the network cache headers and always read from/write to the disk cache.
        .respectCacheHeaders(false)
        // Enable logging to the standard Android log if this is a debug build.
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger(Log.VERBOSE))
            }
        }
        .build()

    companion object {
        private const val COIL_DISK_CACHE_DIRECTORY_NAME = "image_cache"
        private const val COIL_DISK_CACHE_SIZE = 512L * 1024 * 1024  // 512MB
        private const val COIL_MEMORY_CACHE_SIZE_PERCENT = 0.25  // Set the max size to 25% of the app's available memory.
    }
}
