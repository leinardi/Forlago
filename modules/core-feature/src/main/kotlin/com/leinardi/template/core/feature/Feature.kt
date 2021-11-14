/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.core.feature

import android.content.Intent
import androidx.compose.runtime.Composable
import com.leinardi.template.core.navigation.NavigationDestination

abstract class Feature {
    abstract val id: String

    open val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = emptyMap()

    open val debugComposable: @Composable (() -> Unit)? = null

    open val dialogDestinations: Map<NavigationDestination, @Composable () -> Unit> = emptyMap()

    open val serviceLifecycle: FeatureLifecycle = FeatureLifecycle()

    open val handleIntent: (suspend (intent: Intent) -> Unit) = {}
}
