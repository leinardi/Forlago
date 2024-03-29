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

package com.leinardi.forlago.feature.debug.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.leinardi.forlago.feature.debug.api.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Features
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Info
import com.leinardi.forlago.feature.debug.ui.DebugViewModel.DebugNavigationBarItem.Options
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.ui.base.ViewEffect
import com.leinardi.forlago.library.ui.base.ViewEvent
import com.leinardi.forlago.library.ui.base.ViewState
import kotlin.enums.EnumEntries

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
        val certificatePinningEnabled: Boolean = true,
        val environments: EnumEntries<ReadEnvironmentInteractor.Environment> = ReadEnvironmentInteractor.Environment.entries,
        val selectedNavigationItem: DebugViewModel.DebugNavigationBarItem = bottomNavigationItems.first(),
        val testBoolean: Boolean? = null,
        val testDouble: Double? = null,
        val testLong: Long? = null,
        val testString: String? = null,
    ) : ViewState {
        data class Feature(
            val composable: @Composable () -> Unit,
            val featureId: String,
        )
    }

    sealed class Event : ViewEvent {
        data class OnEnableCertificatePinningChanged(val boolean: Boolean) : Event()
        data class OnNavigationBarItemSelected(val selectedNavigationItem: DebugViewModel.DebugNavigationBarItem) : Event()
        data class OnEnvironmentSelected(val environment: ReadEnvironmentInteractor.Environment) : Event()
        data object OnClearApolloCacheClicked : Event()
        data object OnForceCrashClicked : Event()
        data object OnUpButtonClicked : Event()
    }

    sealed class Effect : ViewEffect
}
