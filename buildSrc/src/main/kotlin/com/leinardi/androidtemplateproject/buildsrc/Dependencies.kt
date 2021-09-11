package com.leinardi.androidtemplateproject.buildsrc

object Versions {
    const val buildToolsVersion = "30.0.3"
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
}

@Suppress("MemberNameEqualsClassName")
object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.2"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val versionsGradlePlugin = "com.github.ben-manes:gradle-versions-plugin:0.39.0"
    const val spotlessGradlePlugin = "com.diffplug.spotless:spotless-plugin-gradle:5.15.0"

    object Accompanist {
        const val version = "0.18.0"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val permissions = "com.google.accompanist:accompanist-permissions:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val startup = "androidx.startup:startup-runtime:1.1.0"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.1"
        }

        object Constraint {
            const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.0.2"

            @get:JvmStatic
            val snapshotUrl: String
                get() = "https://androidx.dev/snapshots/builds/$snapshot/artifacts/repository/"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val test = "androidx.compose.test:test-core:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTest = "androidx.compose.ui:ui-test:$version"
        }

        object Lifecycle {
            private const val version = "2.3.1"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Material {
            private const val version = "1.4.0"
            const val material = "com.google.android.material:material:$version"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val safeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        }

        object Room {
            private const val version = "2.3.0"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }
    }

    object Coil {
        const val coilCompose = "io.coil-kt:coil-compose:1.3.0"
    }

    object Coroutines {
        private const val version = "1.5.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics-ktx:19.0.0"
        const val crashlyticsGradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:2.7.1"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx:18.2.0"
    }

    object Detekt {
        const val version = "1.17.1"
        const val formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$version"
        const val gradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$version"
    }

    object Hilt {
        const val version = "2.38.1"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Kotlin {
        private const val version = "1.5.21"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    }

    object JUnit {
        private const val version = "4.13.2"
        const val junit = "junit:junit:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
    }

    object Timber {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
    }
}
