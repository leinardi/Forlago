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

package com.leinardi.forlago.library.feature.interactor

import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.feature.FeatureManager
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class GetFeatureInteractor @Inject constructor(
    private val featureManager: FeatureManager,
) {
    operator fun <T : Feature> invoke(clazz: Class<out Feature>) = featureManager.featureMap.getValue(clazz) as T
}
