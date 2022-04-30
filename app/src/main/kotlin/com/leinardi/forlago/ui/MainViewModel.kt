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
import com.leinardi.forlago.core.android.interactor.android.GetAppUpdateInfoInteractor
import com.leinardi.forlago.core.android.interactor.android.GetAppUpdateInfoInteractor.Result
import com.leinardi.forlago.core.android.interactor.android.GetInstallStateUpdateInteractor
import com.leinardi.forlago.core.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.core.ui.base.BaseViewModel
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
    getInstallStateUpdateInteractor: GetInstallStateUpdateInteractor,
    private val app: Application,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val getAppUpdateInfoInteractor: GetAppUpdateInfoInteractor,
) : BaseViewModel<Event, State, Effect>() {
    @AppUpdateType private var appUpdateType: Int? = null

    init {
        getInstallStateUpdateInteractor().onEach { result ->
            if (result is GetInstallStateUpdateInteractor.Result.Downloaded) {
                sendEffect { Effect.ShowSnackbarForCompleteUpdate }
            }
        }.launchIn(viewModelScope)
        checkForUpdates()
    }

    override fun provideInitialState() = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnInAppUpdateCancelled -> {
                Timber.d("In-App update cancelled")
                if (appUpdateType == AppUpdateType.IMMEDIATE) {
                    // We can't allow the user to skip immediate updates
                    sendEffect { Effect.FinishActivity }
                }
            }
            is Event.OnInAppUpdateFailed -> sendEffect { Effect.ShowErrorSnackbar(app.getString(R.string.i18n_app_update_error)) }
            is Event.OnIntentReceived -> handleOnIntentReceived(event)
            is Event.OnShown -> checkForUpdates(true)
        }
    }

    private fun handleOnIntentReceived(event: Event.OnIntentReceived) {
        viewModelScope.launch {
            getFeaturesInteractor().forEach { feature ->
                feature.handleIntent(event.intent)
            }
        }
    }

    private fun checkForUpdates(checkOnlyInProgress: Boolean = false) {
        viewModelScope.launch {
            val result = getAppUpdateInfoInteractor()
            when {
                result is Result.LowPriorityUpdateAvailable && !checkOnlyInProgress -> Timber.d("App update available but low priority. Ignoring it.")
                result is Result.FlexibleUpdateAvailable && !checkOnlyInProgress -> {
                    Timber.d("MediumPriorityUpdateAvailable")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is Result.ImmediateUpdateAvailable && !checkOnlyInProgress -> {
                    Timber.d("HighPriorityUpdateAvailable")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is Result.DeveloperTriggeredUpdateInProgress -> {
                    Timber.d("DeveloperTriggeredUpdateInProgress")
                    sendEffectStartUpdateFlowForResult(result.appUpdateInfo, result.appUpdateType)
                }
                result is Result.UpdateNotAvailable && !checkOnlyInProgress -> Timber.d("In-App update not available")
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
