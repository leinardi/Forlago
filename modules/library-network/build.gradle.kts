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
    alias(libs.plugins.apollo)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.leinardi.forlago.library.network"
    resourcePrefix = "network_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-network-consumer-rules.pro")
    }
    buildFeatures.buildConfig = true
}

apollo {
    service("forlago") {
        packageNamesFromFilePaths()
        dependsOn(projects.modules.libraryNetworkApi)
    }
}

dependencies {
    api(projects.modules.libraryNetworkApi)
    implementation(projects.modules.libraryAndroidApi)
    implementation(projects.modules.libraryAutobindAnnotation)
    implementation(projects.modules.libraryFeature)
    implementation(projects.modules.libraryI18n)
    implementation(projects.modules.libraryNavigationApi)
    implementation(projects.modules.libraryPreferencesApi)
    implementation(projects.modules.libraryUiApi)
    ksp(projects.modules.libraryAutobindKsp)

    implementation(libs.apollo.cache.sqlite)
    implementation(libs.dagger.hilt.android)
    implementation(libs.kotlinx.serialization)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.timber)
    implementation(libs.tink)
    ksp(libs.dagger.hilt.compiler)

    kspTest(libs.dagger.hilt.android.compiler)
    testImplementation(libs.apollo.testing.support)
    testImplementation(libs.robolectric)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
}
