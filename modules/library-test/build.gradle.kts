/*
 * Copyright 2022 Roberto Leinardi.
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
}

android {
    namespace = "com.leinardi.forlago.library.test"
    resourcePrefix = "test_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-test-consumer-rules.pro")
    }
}

dependencies {
    api(project(":modules:library-android"))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.compose.ui.test.manifest)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.coroutines.test)
    api(libs.hilt.android.testing)
    api(libs.junit)
    api(libs.kotlin.test)
    api(libs.kotlin.test.junit)
    api(libs.okhttp3.mockwebserver)
    kapt(libs.hilt.android.compiler)
}