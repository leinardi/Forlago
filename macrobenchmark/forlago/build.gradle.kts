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

@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import com.leinardi.forlago.ext.configureGradleManagedDevice
import com.leinardi.forlago.model.DeviceConfig

plugins {
    id("com.android.test")
    id("forlago.android-conventions")
    alias(libs.plugins.baselineprofile)
}

val deviceConfig = DeviceConfig(
    device = "Pixel 6",
    apiLevel = 31,
    systemImageSource = "aosp",
)

android {
    namespace = config.apps.forlago.applicationId.get() + ".macrobenchmark"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    testOptions {
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel6Api31") {
                    device = "Pixel 6"
                    apiLevel = 31
                    systemImageSource = "aosp"
                }
            }
        }
    }
    //configureGradleManagedDevice(deviceConfig)

    targetProjectPath = ":apps:forlago"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It's signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }
    }
    buildFeatures.buildConfig = true
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on.
    managedDevices.clear()
    managedDevices += "pixel6Api31"//deviceConfig.deviceName

    // Don't use a connected device but rely on a GMD for consistency between local and CI builds.
    useConnectedDevices = false

}

androidComponents {
    beforeVariants(selector().all()) {
        // enable only the benchmark buildType, since we only want to measure close to release performance
        it.enable = it.buildType == "benchmark"
    }
}

dependencies {
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.test.ext.junit)
}
