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
import com.leinardi.forlago.ext.toSnakeCase
import org.apache.tools.ant.filters.ReplaceTokens
import java.util.Locale


tasks.register("generateFeatureDestinationObject") {
    doLast {
        copy {
            includeEmptyDirs = false
            checkModuleNameProperty(name)

            val tokens = generatePlaceholderTokens()
            val templateDirPath = rootProject.file("modules/.module-template/destination")
            val navigationDir = rootProject.file(
                "modules/library-navigation-api/src/main/kotlin/com/leinardi/forlago/library/navigation/api/destination/${tokens["placeholderlowercase"]}",
            )

            from(templateDirPath)
            into(navigationDir)

            // Directory and file name replacement
            tokens.forEach { (placeholderKey, placeholderValue) ->
                filesMatching("**/$placeholderKey/**/*") {
                    path = path.replace(placeholderKey, placeholderValue)
                }
                rename(placeholderKey, placeholderValue)
            }

            // File extension replacement
            rename(".ktemplate", ".kt")

            // Content replacement
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }
}

tasks.register("generateFeatureApiModule") {
    doLast {
        copy {
            includeEmptyDirs = false
            checkModuleNameProperty(name)

            val tokens = generatePlaceholderTokens()
            val templateDirPath = rootProject.file("modules/.module-template/feature-api")
            val featureDir = rootProject.file("modules/feature-${tokens["placeholderKebabCase"]}-api")

            from(templateDirPath)
            into(featureDir)

            // Directory and file name replacement
            tokens.forEach { (placeholderKey, placeholderValue) ->
                filesMatching("**/$placeholderKey/**/*") {
                    path = path.replace(placeholderKey, placeholderValue)
                }
                rename(placeholderKey, placeholderValue)
            }

            // File extension replacement
            rename(".ktemplate", ".kt")

            // Content replacement
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }
}

tasks.register("generateFeatureModule") {
    dependsOn("generateFeatureApiModule")
    dependsOn("generateFeatureDestinationObject")
    doLast {
        copy {
            includeEmptyDirs = false
            checkModuleNameProperty(name)

            val tokens = generatePlaceholderTokens()
            val templateDirPath = rootProject.file("modules/.module-template/feature")
            val featureDir = rootProject.file("modules/feature-${tokens["placeholderKebabCase"]}")

            from(templateDirPath)
            into(featureDir)

            // Directory and file name replacement
            tokens.forEach { (placeholderKey, placeholderValue) ->
                filesMatching("**/$placeholderKey/**/*") {
                    path = path.replace(placeholderKey, placeholderValue)
                }
                rename(placeholderKey, placeholderValue)
            }

            // File extension replacement
            rename(".ktemplate", ".kt")

            // Content replacement
            filter<ReplaceTokens>("tokens" to tokens)

            printAddModuleHelp(tokens, featureDir)
        }
    }
}

tasks.register("generateLibraryApiModule") {
    doLast {
        copy {
            includeEmptyDirs = false
            checkModuleNameProperty(name)

            val tokens = generatePlaceholderTokens()
            val templateDirPath = rootProject.file("modules/.module-template/library-api")
            val libraryDirPath = rootProject.file("modules/library-${tokens["placeholderKebabCase"]}-api")

            from(templateDirPath)
            into(libraryDirPath)

            // Directory and file name replacement
            tokens.forEach { (placeholderKey, placeholderValue) ->
                filesMatching("**/$placeholderKey/**/*") {
                    path = path.replace(placeholderKey, placeholderValue)
                }
                rename(placeholderKey, placeholderValue)
            }

            // File extension replacement
            rename(".ktemplate", ".kt")

            // Content replacement
            filter<ReplaceTokens>("tokens" to tokens)
        }
    }
}

tasks.register("generateLibraryModule") {
    dependsOn("generateLibraryApiModule")
    doLast {
        copy {
            includeEmptyDirs = false
            checkModuleNameProperty(name)

            val tokens = generatePlaceholderTokens()
            val templateDirPath = rootProject.file("modules/.module-template/library")
            val libraryDirPath = rootProject.file("modules/library-${tokens["placeholderKebabCase"]}")

            from(templateDirPath)
            into(libraryDirPath)

            // Directory and file name replacement
            tokens.forEach { (placeholderKey, placeholderValue) ->
                filesMatching("**/$placeholderKey/**/*") {
                    path = path.replace(placeholderKey, placeholderValue.toString())
                }
                rename(placeholderKey, placeholderValue.toString())
            }

            // File extension replacement
            rename(".ktemplate", ".kt")

            // Content replacement
            filter<ReplaceTokens>("tokens" to tokens)

            printAddModuleHelp(tokens, libraryDirPath)
        }
    }
}

fun printAddModuleHelp(tokens: Map<String, String>, moduleDirPath: File) {
    val tokensUsed = tokens.map { (k, v) -> "            $k: $v" }.joinToString("\n")
    val moduleName = moduleDirPath.name.replace("-" + tokens["placeholderKebabCase"], checkNotNull(tokens["PlaceholderName"]))
    println(
        """
            Module created:
                Name: ${tokens["PlaceholderName"]}
                Package: com.leinardi.forlago.${tokens["placeholderlowercase"]}
                Directory: $moduleDirPath

            Placeholders used:
            $tokensUsed

            1. Declare the module in settings.gradle (alphabetically ordered):
                // Modules
                [...]
                ":modules:${moduleDirPath.name}",
                ":modules:${moduleDirPath.name}-api",
                [...]

            2. Declare the dependency of the module on build.gradle that requires it (alphabetically ordered):
                // Modules
                [...]
                implementation(projects.modules.${moduleName})
                [...]

            3. Synchronize the project for the new module to be available.
        """.trimIndent(),
    )
}

fun generatePlaceholderTokens(): Map<String, String> {
    val moduleName = project.extra.get("moduleName") as String
    return mapOf(
        "PlaceholderName" to moduleName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
        "placeholderUncaptalized" to moduleName.replaceFirstChar { it.lowercase(Locale.ROOT) },
        "PLACEHOLDER_UPPERCASE" to moduleName.toSnakeCase().uppercase(Locale.getDefault()),
        "placeholderlowercase" to moduleName.lowercase(Locale.ROOT),
        "placeholder_snake_case" to moduleName.toSnakeCase(),
        "placeholderKebabCase" to moduleName.toSnakeCase().replace("_", "-"),
    )
}

fun checkModuleNameProperty(taskName: String): Boolean {
    if (!rootProject.hasProperty("moduleName")) {
        throw InvalidUserDataException("e: missing moduleName - Please provide the module name: ./gradlew $taskName -PmoduleName=Foo")
    }
    return true
}
