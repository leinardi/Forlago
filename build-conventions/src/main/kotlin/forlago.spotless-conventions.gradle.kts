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
import com.diffplug.gradle.spotless.SpotlessTask
import com.diffplug.spotless.LineEnding
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.diffplug.spotless")
}

val libs = the<LibrariesForLibs>()

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        diktat(libs.versions.diktat.get()).configFile("$rootDir/config/diktat/diktat-analysis.yml")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    format("graphql") {
        target("**/*.graphql")
        prettier(libs.versions.prettier.get()).configFile("$rootDir/config/prettier/prettierrc-graphql.yml")
    }

    format("yml") {
        target("**/*.yml", "**/*.yaml")
        prettier(libs.versions.prettier.get()).configFile("$rootDir/config/prettier/prettierrc-yml.yml")
    }

    format("androidXml") {
        target("**/AndroidManifest.xml", "src/**/*.xml")
        targetExclude("**/mergedManifests/**/AndroidManifest.xml", "**/build/**/*.xml")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }

    format("misc") {
        // define the files to apply `misc` to
        target("**/*.md", "**/.gitignore")

        // define the steps to apply to those files
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks {
    withType<SpotlessTask> {
        mustRunAfter(":apps:forlago:copyMergedManifests")
    }
}
