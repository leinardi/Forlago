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

package com.leinardi.forlago.core.feature

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeatureManager @Inject constructor() {
    private val _featureMap = mutableMapOf<Class<out Feature>, Feature>()

    val features: List<Feature> get() = _featureMap.entries.map { it.value }

    val featureMap: Map<Class<out Feature>, Feature> = _featureMap

    inline fun <reified T : Feature> getFeature(clazz: Class<out Feature>): T =
        featureMap.getOrElse(clazz) { throw IllegalArgumentException("No Feature with Class = $clazz found!") } as T

    fun register(features: List<Feature>) {
        features.forEach { feature -> _featureMap[feature.javaClass] = feature }
    }

    suspend fun onUserSignIn() = features.map { it.featureLifecycle.onSignIn() }

    suspend fun onUserSignOut() = features.reversed().map { it.featureLifecycle.onSignOut() }
}
