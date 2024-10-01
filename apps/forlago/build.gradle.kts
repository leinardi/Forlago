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
import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import io.github.reactivecircus.appversioning.SemVer
import java.time.Instant
import kotlin.math.log10
import kotlin.math.pow

plugins {
    id("forlago.android-app-conventions")
    alias(libs.plugins.appversioning)
    alias(libs.plugins.tripletplay)
}

val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()
println("Release keystore ${if (useReleaseKeystore) "" else "NOT "}found!")

android {
    defaultConfig {
        applicationId = config.apps.forlago.applicationId.get()
        base.archivesName.set(config.apps.forlago.baseName.get())

        manifestPlaceholders["deepLinkScheme"] = config.apps.forlago.deepLinkScheme.get()
        buildConfigField("String", "DEEP_LINK_SCHEME", "\"${config.apps.forlago.deepLinkScheme.get()}\"")

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
    buildFeatures.buildConfig = true
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

appVersioning {
    overrideVersionCode { gitTag, _, _ ->
        val semVer = SemVer.fromGitTag(gitTag)
        val version = semVer.major * 10000 + semVer.minor * 100 + semVer.patch
        val versionLength = (log10(version.toDouble()) + 1).toInt()
        var epoch = Instant.now().epochSecond.toInt()
        epoch -= epoch % 10.0.pow(versionLength.toDouble()).toInt()
        version + epoch
    }

    overrideVersionName { gitTag, _, variantInfo ->
        "${gitTag.rawTagName}${if (variantInfo.buildType == "debug") "-dev" else ""} (${gitTag.commitHash})"
    }
}

dependencies {
    // Modules
    implementation(projects.modules.featureAccount)
    implementation(projects.modules.featureBar)
    implementation(projects.modules.featureDebug)
    implementation(projects.modules.featureFoo)
    implementation(projects.modules.featureLogin)
    implementation(projects.modules.featureLogout)
    implementation(projects.modules.libraryAndroid)
    implementation(projects.modules.libraryI18n)
    implementation(projects.modules.libraryLogging)
    implementation(projects.modules.libraryNavigation)
    implementation(projects.modules.libraryNetwork)
    implementation(projects.modules.libraryPreferences)
    implementation(projects.modules.libraryRemoteConfig)
    implementation(projects.modules.libraryUi)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.profileinstaller) // Need this to side load a Baseline Profile when Benchmarking
    implementation(libs.androidx.startup)

    kspAndroidTest(libs.dagger.hilt.android.compiler)

    debugImplementation(libs.androidx.compose.tooling)
    debugImplementation(libs.androidx.tracing) // can be removed after the AGP fixes this https://github.com/android/android-test/issues/1755
}
