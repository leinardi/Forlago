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

package com.leinardi.forlago.core.android.interactor.android

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetInstallStateUpdateInteractor @Inject constructor(
    private val appUpdateManager: AppUpdateManager,
) {
    operator fun invoke(): Flow<Result> = callbackFlow {
        val listener = InstallStateUpdatedListener { installState ->
            when (installState.installStatus()) {
                InstallStatus.PENDING -> trySendBlocking(Result.Pending).onFailure { Timber.e(it) }
                InstallStatus.DOWNLOADING -> trySendBlocking(Result.Downloading).onFailure { Timber.e(it) }
                InstallStatus.DOWNLOADED -> trySendBlocking(Result.Downloaded).onFailure { Timber.e(it) }
                InstallStatus.INSTALLING -> trySendBlocking(Result.Installing).onFailure { Timber.e(it) }
                InstallStatus.INSTALLED -> trySendBlocking(Result.Installed).onFailure { Timber.e(it) }
                InstallStatus.FAILED -> trySendBlocking(Result.Failed).onFailure { Timber.e(it) }
                InstallStatus.CANCELED -> trySendBlocking(Result.Canceled).onFailure { Timber.e(it) }
                else -> trySendBlocking(Result.Unknown).onFailure { Timber.e(it) }
            }
        }
        appUpdateManager.registerListener(listener)

        // Suspends until flow collector is cancelled (e.g. by 'take(1)' or because a collector's coroutine was cancelled).
        awaitClose { appUpdateManager.unregisterListener(listener) }
    }

    sealed class Result {
        object Pending : Result()
        object Downloading : Result()
        object Downloaded : Result()
        object Installing : Result()
        object Installed : Result()
        object Failed : Result()
        object Canceled : Result()
        object Unknown : Result()
    }
}
