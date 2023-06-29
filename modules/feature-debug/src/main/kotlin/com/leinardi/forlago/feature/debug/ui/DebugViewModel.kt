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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import com.leinardi.forlago.feature.account.api.interactor.account.LogOutInteractor
import com.leinardi.forlago.feature.debug.interactor.GetDebugInfoInteractor
import com.leinardi.forlago.feature.debug.ui.DebugContract.Effect
import com.leinardi.forlago.feature.debug.ui.DebugContract.Event
import com.leinardi.forlago.feature.debug.ui.DebugContract.State
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result
import com.leinardi.forlago.library.android.api.interactor.android.RestartApplicationInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import com.leinardi.forlago.library.network.api.interactor.ReadCertificatePinningEnabledInteractor
import com.leinardi.forlago.library.network.api.interactor.ReadEnvironmentInteractor
import com.leinardi.forlago.library.network.api.interactor.StoreCertificatePinningEnabledInteractor
import com.leinardi.forlago.library.network.api.interactor.StoreEnvironmentInteractor
import com.leinardi.forlago.library.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor,
    private val getAppUpdateInfoInteractor: GetAppUpdateInfoInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    private val getDebugInfoInteractor: GetDebugInfoInteractor,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val logOutInteractor: LogOutInteractor,
    private val readCertificatePinningEnabledInteractor: ReadCertificatePinningEnabledInteractor,
    private val readEnvironmentInteractor: ReadEnvironmentInteractor,
    private val restartApplicationInteractor: RestartApplicationInteractor,
    private val storeCertificatePinningEnabledInteractor: StoreCertificatePinningEnabledInteractor,
    private val storeEnvironmentInteractor: StoreEnvironmentInteractor,
) : BaseViewModel<Event, State, Effect>() {
    init {
        viewModelScope.launch {
            val result = getAppUpdateInfoInteractor()
            updateState {
                copy(
                    appUpdateInfo = when (result) {
                        is Result.DeveloperTriggeredUpdateInProgress ->
                            "Developer triggered update in progress (priority ${result.appUpdateInfo.updatePriority()})"

                        is Result.FlexibleUpdateAvailable ->
                            "Flexible update available (priority ${result.appUpdateInfo.updatePriority()})"

                        is Result.ImmediateUpdateAvailable ->
                            "Immediate update available (priority ${result.appUpdateInfo.updatePriority()})"

                        is Result.LowPriorityUpdateAvailable ->
                            "Low priority update available (priority ${result.appUpdateInfo.updatePriority()})"

                        is Result.UpdateNotAvailable ->
                            "Update not available"
                    },
                )
            }
        }
    }

    override fun provideInitialState() = State(
        debugInfo = getDebugInfoInteractor(),
        featureList = getFeaturesInteractor()
            .filter { it.debugComposable != null }
            .map { State.Feature(checkNotNull(it.debugComposable), it.id) },
        selectedEnvironment = runBlocking { readEnvironmentInteractor() },  // This runBlocking is acceptable only because it's the debug screen
        certificatePinningEnabled = runBlocking { // This runBlocking is acceptable only because it's the debug screen
            readCertificatePinningEnabledInteractor()
        },
    )

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnNavigationBarItemSelected -> updateState { copy(selectedNavigationItem = event.selectedNavigationItem) }
            is Event.OnClearApolloCacheClicked -> viewModelScope.launch { clearApolloCacheInteractor() }
            is Event.OnEnvironmentSelected -> handleOnEnvironmentSelected(event.environment)
            is Event.OnEnableCertificatePinning -> handleOnEnableCertificatePinning(event.boolean)
            is Event.OnForceCrashClicked -> throw DebugScreenTestCrashException()
            is Event.OnUpButtonClicked -> forlagoNavigator.navigateUp()
        }
    }

    private fun handleOnEnableCertificatePinning(isEnableCertificatePinning: Boolean) {
        viewModelScope.launch {
            storeCertificatePinningEnabledInteractor(isEnableCertificatePinning)
            updateState { copy(certificatePinningEnabled = isEnableCertificatePinning) }
            restartApplicationInteractor()
        }
    }

    private fun handleOnEnvironmentSelected(environment: ReadEnvironmentInteractor.Environment) {
        if (viewState.value.selectedEnvironment != environment) {
            viewModelScope.launch {
                storeEnvironmentInteractor(environment)
                updateState { copy(selectedEnvironment = environment) }
                logOutInteractor()
                restartApplicationInteractor()
            }
        }
    }

    sealed class DebugNavigationBarItem(val label: String, val icon: ImageVector) {
        object Info : DebugNavigationBarItem("Info", Icons.Filled.Info)
        object Options : DebugNavigationBarItem("Options", Icons.Filled.BugReport)
        object Features : DebugNavigationBarItem("Features", Icons.Filled.Star)
    }
}

private class DebugScreenTestCrashException : Exception("Debug screen test crash")
