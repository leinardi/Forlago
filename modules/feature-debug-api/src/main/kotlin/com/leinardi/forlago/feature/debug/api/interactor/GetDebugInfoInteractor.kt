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

package com.leinardi.forlago.feature.debug.api.interactor

interface GetDebugInfoInteractor {
    operator fun invoke(): DebugInfo
    data class DebugInfo(
        val app: App,
        val device: Device,
    ) {
        data class App(
            val name: String,
            val versionName: String,
            val versionCode: Long,
            val packageName: String,
        )

        data class Device(
            val manufacturer: String,
            val model: String,
            val resolutionPx: String,
            val resolutionDp: String,
            val density: Float,
            val scaledDensity: Float,
            val densityDpi: Int,
            val apiLevel: Int,
        )
    }
}
