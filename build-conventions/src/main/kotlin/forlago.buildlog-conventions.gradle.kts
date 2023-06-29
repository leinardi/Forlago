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

import com.leinardi.forlago.ext.config
import com.leinardi.forlago.ext.params
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.logging.LoggingOutputInternal
import java.text.SimpleDateFormat
import java.util.Date

if (config.params.saveBuildLogToFile.get()) {
    val datetime = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date())
    val buildLogDir = "${buildDir}/logs"
    mkdir(buildLogDir)
    val buildLog = File("${buildLogDir}/buildlog-${datetime}.txt")

    System.setProperty("org.gradle.color.error", "RED")

    val outputListener = StandardOutputListener { output -> buildLog.appendText(output.toString()) }
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardOutputListener(outputListener)
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardErrorListener(outputListener)
}
