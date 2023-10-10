/*
 * Copyright 2023 Roberto Leinardi.
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
}

android {
    namespace = "com.leinardi.forlago.library.logging"
    resourcePrefix = "logging_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-logging-consumer-rules.pro")
    }
}

dependencies {
    api(projects.modules.libraryLoggingApi)
    api(libs.timber)
    implementation(libs.androidx.startup)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    if (rootProject.file("apps/forlago/google-services.json").exists()) {
        implementation(libs.firebase.perf)
    }
    implementation(libs.hilt.android)
    implementation(platform(libs.firebase.bom))
    ksp(libs.hilt.compiler)
}
