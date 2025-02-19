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

// Sharing build logic between subprojects
// https://docs.gradle.org/current/samples/sample_convention_plugins.html

plugins {
    `kotlin-dsl`
}


dependencies {
    implementation(libs.apollo.compiler)
    implementation(libs.plugin.aboutlibraries)
    implementation(libs.plugin.android.gradle)
    implementation(libs.plugin.androidcachefix)
    implementation(libs.plugin.compose.compiler)
    implementation(libs.plugin.dagger.hilt)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.easylauncher)
    implementation(libs.plugin.firebase.crashlytics)
    implementation(libs.plugin.firebase.perf)
    implementation(libs.plugin.google.services)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.ksp)
    implementation(libs.plugin.ruler)
    implementation(libs.plugin.spotless)
    implementation(libs.plugin.versions)
    implementation(libs.plugin.versions.update)
    implementation(libs.plugin.violation)
    implementation(platform(libs.firebase.bom))
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
