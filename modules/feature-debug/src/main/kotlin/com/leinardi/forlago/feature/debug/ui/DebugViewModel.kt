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

package com.leinardi.forlago.feature.debug.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.core.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.core.logging.interactor.LogEventScreenViewInteractor
import com.leinardi.forlago.core.navigation.ForlagoNavigator
import com.leinardi.forlago.core.network.interactor.ClearApolloCacheInteractor
import com.leinardi.forlago.core.ui.base.BaseViewModel
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugContract.Effect
import com.leinardi.forlago.feature.debug.ui.DebugContract.Event
import com.leinardi.forlago.feature.debug.ui.DebugContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor,
    private val getDebugInfoInteractor: GetDebugInfoInteractor,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    private val logEventScreenViewInteractor: LogEventScreenViewInteractor,
) : BaseViewModel<Event, State, Effect>() {
    override fun provideInitialState() = State(
        debugInfo = getDebugInfoInteractor(),
        featureList = getFeaturesInteractor()
            .filter { it.debugComposable != null }
            .map { State.Feature(checkNotNull(it.debugComposable), it.id) },
    )

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnBottomNavigationItemSelected ->
                updateState { copy(selectedNavigationItem = event.selectedNavigationItem) }
            is Event.OnUpButtonClicked -> forlagoNavigator.navigateUp()
            is Event.OnForceCrashClicked -> throw IllegalStateException("Debug screen test crash")
            Event.OnClearApolloCacheClicked -> viewModelScope.launch { clearApolloCacheInteractor() }
            Event.OnViewAttached -> logEventScreenViewInteractor("debug_screen", "Debug screen")
        }
    }

    sealed class DebugBottomNavigationItem(val label: String, val icon: ImageVector) {
        object Info : DebugBottomNavigationItem("Info", Icons.Filled.Info)
        object Options : DebugBottomNavigationItem("Options", Icons.Filled.BugReport)
        object Features : DebugBottomNavigationItem("Features", Icons.Filled.Star)
    }
}
