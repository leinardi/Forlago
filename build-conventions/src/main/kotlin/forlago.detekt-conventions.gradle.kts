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
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("io.gitlab.arturbosch.detekt")
}

val libs = the<LibrariesForLibs>()

detekt {
    toolVersion = libs.versions.detekt.get()
    source.setFrom(
        DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
        DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        "src/androidTest/kotlin",
        "src/nativeMain/kotlin",
        "src/nativeTest/kotlin",
    )
    parallel = true
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt)
    detektPlugins(libs.detekt.rules.compose)
    detektPlugins(libs.detekt.nlopez.compose.rules)
}

tasks {
    named("check") {
        dependsOn(withType<Detekt>())
    }
}
