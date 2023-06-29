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
    id("forlago.config-conventions")
    id("forlago.buildlog-conventions")
    id("forlago.generate-module-conventions")
    id("forlago.spotless-conventions")
    id("forlago.versions-conventions")
    id("forlago.dependency-graph-conventions")
    id("forlago.violation-comments-to-github-conventions")
    alias(libs.plugins.gradledoctor)
}


subprojects {
    gradle.projectsEvaluated {
        tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }
}

tasks.withType<Wrapper> {
    description = "Regenerates the Gradle Wrapper files"
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = libs.versions.gradle.get()
}

