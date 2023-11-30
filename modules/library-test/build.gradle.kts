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
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.leinardi.forlago.library.test"
    resourcePrefix = "test_"

    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-test-consumer-rules.pro")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.modules.libraryAndroidApi)

    api(libs.androidx.lifecycle.viewmodel)
    api(libs.androidx.test.core)
    api(libs.androidx.test.runner)
    api(libs.apollo.testing.support)
    api(libs.coroutines.test)
    api(libs.dagger.hilt.android.testing)
    api(libs.junit)
    api(libs.kotlin.test)
    api(libs.kotlin.test.junit)
    api(libs.mockk)
    ksp(libs.dagger.hilt.android.compiler)
}
