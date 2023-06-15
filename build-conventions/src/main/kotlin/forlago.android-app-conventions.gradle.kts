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
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import com.project.starter.easylauncher.filter.ChromeLikeFilter
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.application")
    id("forlago.android-conventions")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.mikepenz.aboutlibraries.plugin")
    id("forlago.merged-manifests-conventions")
    id("forlago.dependencies-conventions")
    id("com.starter.easylauncher")
    id("com.google.firebase.firebase-perf")
    id("forlago.ruler-conventions")
}
val libs = the<LibrariesForLibs>()

val applyGsmServicesPlugins = rootProject.file("app/google-services.json").exists()
if (applyGsmServicesPlugins) {
    project.plugins.apply("com.google.gms.google-services")
    project.plugins.apply("com.google.firebase.crashlytics")
    project.plugins.apply("com.google.firebase.firebase-perf")
}
println("google-services.json ${if (applyGsmServicesPlugins) "" else "NOT "}found!")

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${name}/kotlin")
            }
        }
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.ui.test.ExperimentalTestApi",
        )
    }
}

easylauncher {
    buildTypes {
        create("debug") {
            filters(chromeLike(gravity = ChromeLikeFilter.Gravity.TOP, label = "DEBUG", textSizeRatio = 0.20f, labelPadding = 10))
        }
    }
}

dependencies {
    // Android
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)

    // General
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    implementation(libs.timber)
    debugImplementation(libs.leakcanary)

    api(libs.androidx.compose.ui.test.junit4)
    testImplementation(project(":modules:library-test"))
}

tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/.githooks"))
    into(file("$rootDir/.git/hooks"))
    fileMode = "755".toInt(8)
}

afterEvaluate {
    tasks.named("preBuild").dependsOn("installGitHooks")
}

