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

import com.leinardi.forlago.ext.android
import com.leinardi.forlago.ext.config
import com.leinardi.forlago.ext.configureJavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("forlago.config-conventions")
}

configureJavaCompile()

kotlin {
    sourceSets.all {
        languageSettings.progressiveMode = true // deprecations and bug fixes for unstable code take effect immediately
    }
}

tasks {
    withType<Test> {
        testLogging.events("skipped", "failed")
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = config.android.javaVersion.get().toString()
    }
}
