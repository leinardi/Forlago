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
import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.test")
    id("forlago.android-conventions")
}

android {
    namespace = config.apps.forlago.applicationId.get() + ".macrobenchmark"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
