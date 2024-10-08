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
    id("forlago.android-library-conventions")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.leinardi.forlago.library.remoteconfig"
    resourcePrefix = "remote_config_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-remoteconfig-consumer-rules.pro")
    }
    buildFeatures.buildConfig = true
}
dependencies {
    api(projects.modules.libraryRemoteConfigApi)
    implementation(projects.modules.libraryAutobindAnnotation)
    implementation(projects.modules.libraryLoggingApi)
    ksp(projects.modules.libraryAutobindKsp)

    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.startup)
    implementation(libs.dagger.hilt.android)
    implementation(libs.firebase.config)
    implementation(libs.kotlinx.serialization)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.robolectric)
}
