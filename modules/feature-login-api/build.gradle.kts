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
}

android {
    namespace = "com.leinardi.forlago.feature.login.api"
    resourcePrefix = "login_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-login-api-consumer-rules.pro")
    }
}

dependencies {
    api(projects.modules.featureAccountApi)
    implementation(projects.modules.libraryNavigationApi)
    implementation(projects.modules.libraryNetworkApi)
    ksp(projects.modules.libraryNavigationKsp)
}

// Workaround for https://github.com/detekt/detekt/issues/4743
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("com/leinardi/forlago/feature/login/api/destination/*Destination.kt")
}
