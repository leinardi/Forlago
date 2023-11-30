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
    id("com.android.library")
    id("forlago.android-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.test.android"
    resourcePrefix = "test_android_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-test-android-consumer-rules.pro")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.compose.ui.test.manifest)
    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.uiautomator)
    api(libs.kotlin.faker)
    api(libs.okhttp3.mockwebserver)
    api(libs.turingcomplete.onetimepassword)
}
