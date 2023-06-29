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

package com.leinardi.forlago.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.AppUpdateType
import com.leinardi.forlago.R
import com.leinardi.forlago.library.android.api.ext.ifTrue
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result.DeveloperTriggeredUpdateInProgress
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result.FlexibleUpdateAvailable
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result.ImmediateUpdateAvailable
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result.LowPriorityUpdateAvailable
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result.UpdateNotAvailable
import com.leinardi.forlago.library.android.api.interactor.android.GetInstallStateUpdateFlowInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.preferences.api.interactor.GetMaterialYouFlowInteractor
import com.leinardi.forlago.library.preferences.api.interactor.GetThemeFlowInteractor
import com.leinardi.forlago.library.ui.base.BaseViewModel
import com.leinardi.forlago.library.ui.interactor.SetNightModeInteractor
import com.leinardi.forlago.ui.MainContract.Effect
import com.leinardi.forlago.ui.MainContract.Event
import com.leinardi.forlago.ui.MainContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getInstallStateUpdateFlowInteractor: GetInstallStateUpdateFlowInteractor,
    getMaterialYouFlowInteractor: GetMaterialYouFlowInteractor,
    getThemeFlowInteractor: GetThemeFlowInteractor,
    private val app: Application,
    private val getAppUpdateInfoInteractor: GetAppUpdateInfoInteractor,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    setNightModeInteractor: SetNightModeInteractor,
) : BaseViewModel<Event, State, Effect>() {
    @AppUpdateType private var appUpdateType: Int? = null

    init {
        getThemeFlowInteractor()
            .onEach { setNightModeInteractor(it) }
            .launchIn(viewModelScope)
        getMaterialYouFlowInteractor()
            .onEach { updateState { copy(dynamicColors = it) } }
            .launchIn(viewModelScope)
        getInstallStateUpdateFlowInteractor()
            .onEach { result ->
                if (result is GetInstallStateUpdateFlowInteractor.Result.Downloaded) {
                    sendEffect { Effect.ShowSnackbarForCompleteUpdate }
                }
            }
            .launchIn(viewModelScope)
        checkForUpdates()
    }

    override fun provideInitialState() = State(forlagoNavigator.homeDestination)

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnInAppUpdateCancelled -> {
                Timber.d("In-App update cancelled")
                if (appUpdateType == AppUpdateType.IMMEDIATE) {
                    // We can't allow the user to skip immediate updates
                    sendEffect { Effect.FinishActivity }
                }
            }
            is Event.OnInAppUpdateFailed -> sendEffect { Effect.ShowErrorSnackbar(
                app.getString(com.leinardi.forlago.library.i18n.R.string.i18n_app_update_error),
            ) }
            is Event.OnIntentReceived -> handleOnIntentReceived(event)
            is Event.OnShown -> checkForUpdates(true)
        }
    }

    private fun handleOnIntentReceived(event: Event.OnIntentReceived) {
        viewModelScope.launch {
            var handled = false
            getFeaturesInteractor().forEach { feature ->
                if (!handled) {
                    feature.handleIntent(event.intent).ifTrue { handled = true }
                }
            }
            if (!handled && event.isNewIntent) {
                forlagoNavigator.handleDeepLink(event.intent)
            }
        }
    }

    private fun checkForUpdates(checkOnlyInProgress: Boolean = false) {
        viewModelScope.launch {
            val result = getAppUpdateInfoInteractor()
            when {
                result is LowPriorityUpdateAvailable && !checkOnlyInProgress -> Timber.d("App update available but low priority. Ignoring it.")
                result is FlexibleUpdateAvailable && !checkOnlyInProgress -> {
                    Timber.d("MediumPriorityUpdateAvailable")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is ImmediateUpdateAvailable && !checkOnlyInProgress -> {
                    Timber.d("HighPriorityUpdateAvailable")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is DeveloperTriggeredUpdateInProgress -> {
                    Timber.d("DeveloperTriggeredUpdateInProgress")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is UpdateNotAvailable && !checkOnlyInProgress -> Timber.d("In-App update not available")
            }
        }
    }

    private fun sendEffectStartUpdateFlowForResult(
        appUpdateInfo: AppUpdateInfo,
        @AppUpdateType appUpdateType: Int,
    ) {
        this.appUpdateType = appUpdateType
        sendEffect { Effect.StartUpdateFlowForResult(appUpdateInfo, appUpdateType) }
    }
}
