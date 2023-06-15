/*
 * Copyright 2022 Roberto Leinardi.
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

/**
 * Taken from https://github.com/JakeWharton/SdkSearch/blob/master/gradle/projectDependencyGraph.gradle
 */

import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.io.BufferedReader
import java.util.Locale

val font = "Helvetica"

tasks.register("projectDependencyGraph") {
    doLast {
        val dot = File(rootProject.buildDir, "reports/dependencyGraph/project.dot")
        dot.parentFile.mkdirs()
        dot.delete()

        dot.writeText("digraph {\n")
        dot.appendText("  graph [label=\"${rootProject.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4,fontname=\"$font\"];\n")
        dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\", fontname=\"$font\"];\n")
        dot.appendText("  edge [fontname = \"$font\"];")
        dot.appendText("  rankdir=TB;\n")

        val rootProjects = mutableListOf<Project>()
        val queue = mutableListOf(rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeFirst()
            rootProjects.add(project)
            queue.addAll(project.childProjects.values)
        }

        val projects = LinkedHashSet<Project>()
        val dependencies = LinkedHashMap<Pair<Project, Project>, List<String>>()
        val androidProjects = mutableListOf<Project>()
        val apiProjects = mutableListOf<Project>()
        val featureProjects = mutableListOf<Project>()
        val javaProjects = mutableListOf<Project>()
        val jsProjects = mutableListOf<Project>()
        val libraryProjects = mutableListOf<Project>()
        val multiplatformProjects = mutableListOf<Project>()

        queue.clear()
        queue.add(rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeFirst()
            queue.addAll(project.childProjects.values)

            if (project.name.endsWith("-api")) {
                apiProjects.add(project)
            } else {
                if (project.plugins.hasPlugin("forlago.android-feature-conventions")) {
                    featureProjects.add(project)
                }
                if (project.plugins.hasPlugin("forlago.android-library-conventions")) {
                    libraryProjects.add(project)
                }
                if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    multiplatformProjects.add(project)
                }
                if (project.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                    jsProjects.add(project)
                }
                if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("com.android.application")) {
                    androidProjects.add(project)
                }
                if (project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                    javaProjects.add(project)
                }
            }

            project.configurations.forEach { config ->
                config.dependencies
                    .withType(ProjectDependency::class.java)
                    .forEach { dependency ->
                        if (project != rootProject) {
                            projects.add(project)
                            projects.add(dependency.dependencyProject)
                            rootProjects.remove(dependency.dependencyProject)

                            val graphKey = project to dependency.dependencyProject
                            if (project != dependency.dependencyProject) {
                                val traits = dependencies.computeIfAbsent(graphKey) { mutableListOf() }.toMutableList()
                                if (config.name.lowercase(Locale.ROOT).endsWith("implementation")) {
                                    traits.add("style=dotted")
                                }
                            }
                        }
                    }
            }
        }

        projects.sortedBy { it.path }

        dot.appendText("\n  # Projects\n\n")
        for (project in projects) {
            val traits = mutableListOf<String>()

            if (rootProjects.contains(project)) {
                traits.add("shape=box")
            }

            when {
                apiProjects.contains(project) -> traits.add("fillcolor=\"#F0F4C3\"")
                featureProjects.contains(project) -> traits.add("fillcolor=\"#689F38\"")
                libraryProjects.contains(project) -> traits.add("fillcolor=\"#AED581\"")
                multiplatformProjects.contains(project) -> traits.add("fillcolor=\"#A69BE4\"")
                jsProjects.contains(project) -> traits.add("fillcolor=\"#F0DB4F\"")
                androidProjects.contains(project) -> traits.add("fillcolor=\"#8BC34A\"")
                javaProjects.contains(project) -> traits.add("fillcolor=\"#FF9800\"")
                else -> traits.add("fillcolor=\"#EEEEEE\"")
            }

            dot.appendText("  \"${project.path}\" [${traits.joinToString(", ")}];\n")
        }

        dot.appendText("\n  {rank = same;")
        for (project in projects) {
            if (rootProjects.contains(project)) {
                dot.appendText(" \"${project.path}\";")
            }
        }
        dot.appendText("}\n")

        dot.appendText("\n  # Dependencies\n\n")
        dependencies.forEach { (key, traits) ->
            dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
            if (traits.isNotEmpty()) {
                dot.appendText(" [${traits.joinToString(", ")}]")
            }
            dot.appendText("\n")
        }

        dot.appendText("}\n")

        val p = Runtime.getRuntime().exec(arrayOf("dot", "-Tpng", "-O", "project.dot"), emptyArray(), dot.parentFile)
        p.waitFor()
        require(p.exitValue() == 0) { p.errorStream.bufferedReader().use(BufferedReader::readText) }

        println("Project module dependency graph created at ${dot.absolutePath}.png")

        copy {
            from("${dot.absolutePath}.png")
            into(rootProject.file("art"))
        }
    }
}

// Add the task as check dependency only if dot is available
@Suppress("TooGenericExceptionCaught")
try {
    val p = Runtime.getRuntime().exec(arrayOf("dot", "-V"), emptyArray())
    p.waitFor()
    if (p.exitValue() == 0) {
        tasks.named("check").dependsOn("projectDependencyGraph")
    } else {
        logger.warn("w: {}", p.errorStream.bufferedReader().use(BufferedReader::readText))
    }
} catch (e: Exception) {
    logger.warn("w: {}", e.message)
}
