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

package com.leinardi.forlago.feature.debug.interactor

import android.app.Application
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.leinardi.forlago.feature.debug.api.destination.DebugDestination
import com.leinardi.forlago.feature.debug.api.interactor.DebugShakeDetectorInteractor
import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.squareup.seismic.ShakeDetector
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@AutoBind([ActivityRetainedComponent::class])
@ActivityRetainedScoped
internal class DebugShakeDetectorInteractorImpl @Inject constructor(
    application: Application,
    private val forlagoNavigator: ForlagoNavigator,
) : DebugShakeDetectorInteractor, DefaultLifecycleObserver {
    private val sensorManager = ContextCompat.getSystemService(application, SensorManager::class.java)

    private val shakeDetector by lazy {
        ShakeDetector {
            ProcessLifecycleOwner.get().lifecycleScope.launch {
                forlagoNavigator.navigate(DebugDestination.get())
            }
        }
    }

    override fun startObserving() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun stopObserving() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        shakeDetector.start(checkNotNull(sensorManager), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        shakeDetector.stop()
    }
}
