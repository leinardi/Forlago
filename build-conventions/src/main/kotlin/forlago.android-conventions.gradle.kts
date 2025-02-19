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
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.leinardi.forlago.ext.android
import com.leinardi.forlago.ext.config
import com.leinardi.forlago.ext.configureJavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-android")
    id("forlago.detekt-conventions")
    id("forlago.config-conventions")
}

plugins.withType<AndroidBasePlugin>().configureEach {
    extensions.configure<BaseExtension> {
        compileSdkVersion(config.android.compileSdk.get())

        defaultConfig {
            minSdk = config.android.minSdk.get()
            targetSdk = config.android.targetSdk.get()

            testInstrumentationRunner = "com.leinardi.forlago.library.test.runner.HiltTestRunner"
            // The following argument makes the Android Test Orchestrator run its
            // "pm clear" command after each test invocation. This command ensures
            // that the app's state is completely cleared between tests.
            setTestInstrumentationRunnerArguments(mutableMapOf("clearPackageData" to "true"))
        }

        compileOptions {
            sourceCompatibility = config.android.javaVersion.get()
            targetCompatibility = config.android.javaVersion.get()
        }

        testOptions {
            animationsDisabled = true
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }

        if (this is CommonExtension<*, *, *, *, *, *>) {
            lint {
                abortOnError = true
                checkAllWarnings = false
                checkDependencies = true
                checkReleaseBuilds = false
                ignoreTestSources = true
                warningsAsErrors = false
                disable.add("ResourceType")
                lintConfig = file("${project.rootDir}/config/lint/lint.xml")
            }

            configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    freeCompilerArgs.set(
                        freeCompilerArgs.get() + listOf(
                            "-opt-in=kotlin.RequiresOptIn",
                            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                            "-opt-in=kotlinx.coroutines.FlowPreview",
                        ),
                    )
                    jvmTarget.set(JvmTarget.fromTarget(config.android.javaVersion.get().toString()))
                }
            }
        }

        packagingOptions {
            resources {
                // Use this block to exclude conflicting files that breaks your APK assemble task
                excludes.add("META-INF/LICENSE-notice.md")
                excludes.add("META-INF/LICENSE.md")
                excludes.add("META-INF/NOTICE.md")
            }
        }
    }
}

configureJavaCompile()

kotlin {
    sourceSets.all {
        languageSettings.progressiveMode = true // deprecations and bug fixes for unstable code take effect immediately
    }
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(config.android.javaVersion.get().toString()))
        }
    }

    withType<Test> {
        testLogging.events("skipped", "failed")
    }
}
