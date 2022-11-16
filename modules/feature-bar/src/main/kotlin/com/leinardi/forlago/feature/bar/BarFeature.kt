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

package com.leinardi.forlago.feature.bar

import androidx.compose.runtime.Composable
import com.leinardi.forlago.feature.bar.ui.BarScreen
import com.leinardi.forlago.feature.bar.ui.debug.BarDebugPage
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.navigation.api.destination.NavigationDestination
import com.leinardi.forlago.library.navigation.api.destination.bar.BarDestination

class BarFeature : Feature() {
    override val id = "Bar"

    override val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
        BarDestination to { BarScreen() },
    )

    override val debugComposable: @Composable () -> Unit = { BarDebugPage() }
}
