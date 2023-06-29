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

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// https://docs.gradle.org/7.0/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Forlago"

includeBuild("build-conventions")

include(

        // Apps
        ":apps:forlago",

        // Baseline profiles
        ":macrobenchmark:forlago",

        // Modules
        ":modules:feature-account",
        ":modules:feature-account-api",
        ":modules:feature-bar",
        ":modules:feature-debug",
        ":modules:feature-foo",
        ":modules:library-android",
        ":modules:library-android-api",
        ":modules:library-feature",
        ":modules:library-i18n",
        ":modules:library-logging",
        ":modules:library-logging-api",
        ":modules:library-navigation",
        ":modules:library-navigation-annotation",
        ":modules:library-navigation-api",
        ":modules:library-navigation-ksp",
        ":modules:library-network",
        ":modules:library-network-api",
        ":modules:library-preferences",
        ":modules:library-preferences-api",
        ":modules:library-test",
        ":modules:library-ui",
        ":modules:library-ui-api",
        )
