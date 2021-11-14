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

package com.leinardi.template.feature.debug.initializer

import android.content.Context
import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.Initializer
import com.leinardi.template.feature.debug.BuildConfig
import com.leinardi.template.feature.debug.di.DebugInitializerEntryPoint
import com.leinardi.template.core.navigation.TemplateNavigator
import com.leinardi.template.core.navigation.destination.debug.DebugDestination
import com.squareup.seismic.ShakeDetector
import javax.inject.Inject

/**
 * Initializes a shake detector that will show the Debug screen when the device is shaken with the app on foreground
 */
class DebugInitializer : Initializer<ShakeDetector?>, DefaultLifecycleObserver {
    @Inject lateinit var navigator: TemplateNavigator
    private var sensorManager: SensorManager? = null

    private val shakeDetector by lazy {
        ShakeDetector {
            navigator.navigate(DebugDestination.createRoute())
        }
    }

    override fun create(context: Context): ShakeDetector {
        DebugInitializerEntryPoint.resolve(context).inject(this)
        if (BuildConfig.DEBUG) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
        return shakeDetector
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        shakeDetector.start(checkNotNull(sensorManager), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        shakeDetector.stop()
    }
}
