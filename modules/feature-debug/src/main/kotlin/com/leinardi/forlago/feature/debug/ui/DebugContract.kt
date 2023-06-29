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

package com.leinardi.forlago.feature.debug.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Features
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Info
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Options
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.ui.base.ViewEffect
import com.leinardi.forlago.library.ui.base.ViewEvent
import com.leinardi.forlago.library.ui.base.ViewState

@Immutable
object DebugContract {
    data class State(
        val debugInfo: GetDebugInfoInteractor.DebugInfo,
        val featureList: List<Feature>,
        val selectedEnvironment: ReadEnvironmentInteractor.Environment,
        val appUpdateInfo: String = "...",
        val bottomNavigationItems: List<DebugViewModel.DebugNavigationBarItem> = listOf(
            Info,
            Options,
            Features,
        ),
        val environments: Array<ReadEnvironmentInteractor.Environment> = ReadEnvironmentInteractor.Environment.values(),
        val selectedNavigationItem: DebugViewModel.DebugNavigationBarItem = bottomNavigationItems[0],
        val certificatePinningEnabled: Boolean = true,
    ) : ViewState {
        data class Feature(
            val composable: @Composable () -> Unit,
            val featureId: String,
        )
    }

    sealed class Event : ViewEvent {
        data class OnNavigationBarItemSelected(val selectedNavigationItem: DebugViewModel.DebugNavigationBarItem) : Event()
        data class OnEnvironmentSelected(val environment: ReadEnvironmentInteractor.Environment) : Event()
        data class OnEnableCertificatePinning(val boolean: Boolean) : Event()
        object OnClearApolloCacheClicked : Event()
        object OnForceCrashClicked : Event()
        object OnUpButtonClicked : Event()
    }

    sealed class Effect : ViewEffect
}
