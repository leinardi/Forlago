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

import io.github.reactivecircus.appversioning.SemVer
import java.time.Instant
import kotlin.math.log10
import kotlin.math.pow

plugins {
    id("io.github.reactivecircus.app-versioning")
}

appVersioning {
    overrideVersionCode { gitTag, _, _ ->
        val semVer = SemVer.fromGitTag(gitTag)
        val version = semVer.major * 10000 + semVer.minor * 100 + semVer.patch
        val versionLength = (log10(version.toDouble()) + 1).toInt()
        var epoch = Instant.now().epochSecond.toInt()
        epoch -= epoch % 10.0.pow(versionLength.toDouble()).toInt()
        version + epoch
    }
    overrideVersionName { gitTag, _, variantInfo ->
        "${gitTag.rawTagName}${if (variantInfo.buildType == "debug") "-dev" else ""} (${gitTag.commitHash})"
    }
}
