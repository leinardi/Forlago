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

import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("forlago.android-library-conventions")
}

android {
    namespace = "com.leinardi.forlago.library.navigation.api"
    resourcePrefix = "navigation_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-navigation-api-consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.modules.libraryNavigationAnnotation)
    ksp(projects.modules.libraryNavigationKsp)

    api(libs.androidx.navigation.compose)
}

// Workaround for https://github.com/detekt/detekt/issues/4743
tasks.withType<Detekt>().configureEach {
    exclude("com/leinardi/forlago/library/navigation/api/destination/**/*Destination.kt")
}

afterEvaluate {
    tasks.named("compileDebugKotlin").configure { shouldRunAfter(tasks.named("kspDebugKotlin")) }
}
