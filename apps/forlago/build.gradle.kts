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
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    id("forlago.android-app-conventions")
    id("forlago.app-versioning-conventions")
    alias(libs.plugins.tripletplay)
}

val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()
println("Release keystore ${if (useReleaseKeystore) "" else "NOT "}found!")

android {
    defaultConfig {
        applicationId = config.apps.forlago.applicationId.get()
        setProperty("archivesBaseName", "forlago")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"  // https://github.com/google/dagger/issues/2033

        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = project.properties["RELEASE_KEYSTORE_PWD"] as String
                keyAlias = "release"
                keyPassword = project.properties["RELEASE_KEYSTORE_PWD"] as String
            }
        }
    }

    buildTypes {
        getByName("debug") {
            namespace = config.apps.forlago.applicationId.get() + ".debug"
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            namespace = config.apps.forlago.applicationId.get()
            if (useReleaseKeystore) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                // Otherwise just use the debug keystore (this is mainly for PR CI builds)
                signingConfig = signingConfigs.getByName("debug")
            }
            isProfileable = true
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")

            matchingFallbacks.addAll(listOf("release"))
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules-benchmark.pro")
        }
    }
}

val serviceAccountCredentialsFile: File = rootProject.file("release/play-account.json")
if (serviceAccountCredentialsFile.exists()) {
    play {
        serviceAccountCredentials.set(serviceAccountCredentialsFile)
        // Uncomment once the app is published to the store
        releaseStatus.set(/*if (track.get() == "internal") ReleaseStatus.COMPLETED else */ReleaseStatus.DRAFT)
        defaultToAppBundles.set(true)
    }
}
println("play-account.json ${if (serviceAccountCredentialsFile.exists()) "" else "NOT "}found!")

dependencies {
    // Modules
    implementation(projects.modules.featureAccount)
    implementation(projects.modules.featureBar)
    implementation(projects.modules.featureDebug)
    implementation(projects.modules.featureFoo)
    implementation(projects.modules.libraryAndroid)
    implementation(projects.modules.libraryI18n)
    implementation(projects.modules.libraryLogging)
    implementation(projects.modules.libraryNavigation)
    implementation(projects.modules.libraryNetwork)
    implementation(projects.modules.libraryPreferences)
    implementation(projects.modules.libraryUi)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.profileinstaller) // Need this to side load a Baseline Profile when Benchmarking
    implementation(libs.androidx.startup)

    kaptAndroidTest(libs.hilt.android.compiler)

    debugImplementation(libs.androidx.compose.tooling)
    debugImplementation(libs.androidx.tracing) // can be removed after the AGP fixes this https://github.com/android/android-test/issues/1755
}
