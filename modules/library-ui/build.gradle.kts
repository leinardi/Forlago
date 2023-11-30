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
    namespace = "com.leinardi.forlago.library.ui"
    resourcePrefix = "ui_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-ui-consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(projects.modules.libraryUiApi)
    implementation(projects.modules.libraryAndroidApi)
    implementation(projects.modules.libraryI18n)
    implementation(projects.modules.libraryNavigationApi)
    implementation(projects.modules.libraryPreferencesApi)
    api(libs.accompanist.navigation.material)
    api(libs.accompanist.placeholder)
    api(libs.androidx.appcompat)
    api(libs.androidx.compose.material) // Still needed for stuff missing in M3, like ModalBottomSheetLayout
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.window.size)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
    api(libs.androidx.compose.tooling)
    api(libs.androidx.compose.ui)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.core.splashscreen)
    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.paging)
    api(libs.coil.compose)
    api(libs.material)
    api(libs.zoomable)
    implementation(libs.dagger.hilt.android)
    debugApi(libs.androidx.customview)  // Workaround for https://issuetracker.google.com/issues/227767363
    debugApi(libs.androidx.customview.poolingcontainer)  // Workaround for https://issuetracker.google.com/issues/227767363
    ksp(libs.dagger.hilt.compiler)
}
