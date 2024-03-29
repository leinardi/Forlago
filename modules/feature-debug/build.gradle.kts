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

plugins {
    id("forlago.android-feature-conventions")
}

android {
    namespace = "com.leinardi.forlago.feature.debug"
    resourcePrefix = "debug_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-debug-consumer-rules.pro")
    }
    buildFeatures.buildConfig = true
}

dependencies {
    api(projects.modules.featureDebugApi)
    implementation(projects.modules.featureAccountApi)
    implementation(projects.modules.featureLogoutApi)
    implementation(projects.modules.libraryNetworkApi)
    implementation(projects.modules.libraryPreferencesApi)
    implementation(projects.modules.libraryRemoteConfigApi)

    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.startup)
    implementation(libs.seismic)
}
