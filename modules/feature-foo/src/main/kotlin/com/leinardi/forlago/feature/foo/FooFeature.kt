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

package com.leinardi.forlago.feature.foo

import androidx.compose.runtime.Composable
import com.leinardi.forlago.core.feature.Feature
import com.leinardi.forlago.core.navigation.NavigationDestination
import com.leinardi.forlago.core.navigation.destination.foo.FooDestination
import com.leinardi.forlago.feature.foo.ui.FooScreen
import com.leinardi.forlago.feature.foo.ui.debug.FooDebugPage

class FooFeature : Feature() {
    override val id = "Foo"

    override val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
        FooDestination to { FooScreen() },
    )

    override val debugComposable: @Composable () -> Unit = { FooDebugPage() }
}
