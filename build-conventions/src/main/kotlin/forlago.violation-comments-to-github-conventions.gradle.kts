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
import se.bjurr.violations.comments.github.plugin.gradle.ViolationCommentsToGitHubTask

plugins {
    id("se.bjurr.violations.violation-comments-to-github-gradle-plugin")
}

tasks.register<ViolationCommentsToGitHubTask>("violationCommentsToGitHub") {
    notCompatibleWithConfigurationCache("https://github.com/tomasbjerre/violation-comments-to-github-gradle-plugin/issues/13")
    setRepositoryOwner("leinardi")
    setRepositoryName("Forlago")
    setPullRequestId(System.getProperties()["GITHUB_PULLREQUESTID"] as? String)
    setoAuth2Token(System.getProperties()["GITHUB_OAUTH2TOKEN"] as? String)
    setGitHubUrl("https://api.github.com/")
    setCreateCommentWithAllSingleFileComments(false)
    setCreateSingleFileComments(true)
    setCommentOnlyChangedContent(true)
    setKeepOldComments(false)
    setViolations(
        listOf(
            listOf(
                "KOTLINGRADLE",
                ".",
                ".*/build/logs/buildlog.*\\.txt\$",
                "Gradle",
            ),
            listOf(
                "CHECKSTYLE",
                ".",
                ".*/reports/detekt/.*\\.xml\$",
                "Detekt",
            ),
            listOf(
                "ANDROIDLINT",
                ".",
                ".*/reports/lint-results.*\\.xml\$",
                "Android Lint",
            ),
            listOf(
                "JUNIT",
                ".",
                ".*/build/test-results/test.*/.*\\.xml\$",
                "JUnit",
            ),
            listOf(
                "JUNIT",
                ".",
                ".*/build/sauce/saucectl-report\\.xml\$",
                "Espresso",
            ),
        ),
    )
}
