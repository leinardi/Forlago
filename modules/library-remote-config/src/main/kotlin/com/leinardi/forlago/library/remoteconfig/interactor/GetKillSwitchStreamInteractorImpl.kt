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

package com.leinardi.forlago.library.remoteconfig.interactor

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.configUpdates
import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.remoteconfig.api.interactor.GetFeatureFlagInteractor
import com.leinardi.forlago.library.remoteconfig.api.interactor.GetKillSwitchStreamInteractor
import com.leinardi.forlago.library.remoteconfig.api.model.RemoteConfigValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
internal class GetKillSwitchStreamInteractorImpl @Inject constructor(
    application: Application,
    private val getFeatureFlagInteractor: GetFeatureFlagInteractor,
) : GetKillSwitchStreamInteractor {
    private val isFirebaseInitialized = FirebaseApp.getApps(application).isNotEmpty()
    private var configUpdatesFlow: SharedFlow<ConfigUpdate> = MutableSharedFlow()

    init {
        if (isFirebaseInitialized) {
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            configUpdatesFlow = remoteConfig.configUpdates
                .onEach { remoteConfig.activate().await() }
                .shareIn(
                    scope = ProcessLifecycleOwner.get().lifecycleScope,
                    started = SharingStarted.Eagerly,
                )
        }
    }

    override fun getBoolean(key: String): Flow<RemoteConfigValue.Boolean> =
        getConfigUpdatesStream(key) {
            getFeatureFlagInteractor.getBoolean(key)
        }

    override fun getByteArray(key: String): Flow<RemoteConfigValue.ByteArray> =
        getConfigUpdatesStream(key) {
            getFeatureFlagInteractor.getByteArray(key)
        }

    override fun getDouble(key: String): Flow<RemoteConfigValue.Double> =
        getConfigUpdatesStream(key) {
            getFeatureFlagInteractor.getDouble(key)
        }

    override fun getLong(key: String): Flow<RemoteConfigValue.Long> =
        getConfigUpdatesStream(key) {
            getFeatureFlagInteractor.getLong(key)
        }

    override fun getString(key: String): Flow<RemoteConfigValue.String> =
        getConfigUpdatesStream(key) {
            getFeatureFlagInteractor.getString(key)
        }

    private fun <T> getConfigUpdatesStream(key: String, onActivated: () -> T): Flow<T> =
        configUpdatesFlow
            .onSubscription { emit(ConfigUpdate.create(setOf(key))) }  // emit current local value on subscription
            .filter { it.updatedKeys.contains(key) }
            .map { onActivated() }
}
