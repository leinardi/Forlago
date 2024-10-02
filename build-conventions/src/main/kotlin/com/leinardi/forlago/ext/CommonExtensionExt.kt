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

package com.leinardi.forlago.ext

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import com.leinardi.forlago.model.DeviceConfig
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke

/**
 * Configure project for Gradle managed devices
 */
@Suppress("UnstableApiUsage")
fun CommonExtension<*, *, *, *, *, *>.configureGradleManagedDevices(
    minApiLevel: Int,
    maxApiLevel: Int,
    device: String = "Pixel 2",
    systemImageSource: String = "google",
) {
    val allDevices: List<DeviceConfig> = (minApiLevel.coerceAtLeast(MIN_SUPPORTED_GMD_API_LEVEL)..maxApiLevel).map { apiLevel ->
        DeviceConfig(device, apiLevel, systemImageSource)
    }

    testOptions {
        managedDevices {
            devices {
                allDevices.forEach { deviceConfig ->
                    create<ManagedVirtualDevice>(deviceConfig.deviceName).apply {
                        this.device = deviceConfig.device
                        apiLevel = deviceConfig.apiLevel
                        this.systemImageSource = deviceConfig.systemImageSource
                    }
                }
            }
        }
    }
}

fun CommonExtension<*, *, *, *, *, *>.configureGradleManagedDevice(
    deviceConfig: DeviceConfig,
) {
    configureGradleManagedDevices(
        minApiLevel = deviceConfig.apiLevel,
        maxApiLevel = deviceConfig.apiLevel,
        device = deviceConfig.device,
        systemImageSource = deviceConfig.systemImageSource,
    )
}

private const val MIN_SUPPORTED_GMD_API_LEVEL = 27
