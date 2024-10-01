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

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("forlago.android-library-conventions")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = the<LibrariesForLibs>()

android {
    buildFeatures {
        compose = true
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        )
    }
}

tasks {
    withType<Test> {
        // Avoid to run each unit test twice since on feature modules we do not have release specific code
        if (name.endsWith("ReleaseUnitTest")) {
            enabled = false
        }
    }

    // Workaround for https://github.com/detekt/detekt/issues/4743
    withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude("**/*AutoBindModule.kt")
    }
}

dependencies {
    api(project(":modules:library-feature"))
    implementation(project(":modules:library-android-api"))
    implementation(project(":modules:library-autobind-annotation"))
    implementation(project(":modules:library-i18n"))
    implementation(project(":modules:library-logging-api"))
    implementation(project(":modules:library-ui"))
    ksp(project(":modules:library-autobind-ksp"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.dagger.hilt.android)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.timber)
    ksp(libs.dagger.hilt.compiler)

    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(project(":modules:library-test-android"))
    androidTestUtil(libs.androidx.test.orchestrator)
}
