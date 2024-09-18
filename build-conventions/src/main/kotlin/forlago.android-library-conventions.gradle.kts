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
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("forlago.android-conventions")
}

val libs = the<LibrariesForLibs>()

android {
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    kotlin {
        sourceSets {
            named("debug") {
                kotlin.srcDir("build/generated/ksp/debug/kotlin")
            }
            named("release") {
                kotlin.srcDir("build/generated/ksp/release/kotlin")
            }
        }
    }
}

tasks {
    withType<Test> {
        // Avoid to run each unit test twice since on library modules we do not have release specific code
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)

    androidTestImplementation(project(":modules:library-test-android"))
    androidTestUtil(libs.androidx.test.orchestrator)

    testImplementation(project(":modules:library-test"))
}
