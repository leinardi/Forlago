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

package com.leinardi.forlago.library.android.interactor.android

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor
import com.leinardi.forlago.library.android.api.interactor.android.GetAppUpdateInfoInteractor.Result
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetAppUpdateInfoInteractorImpl @Inject constructor(
    private val appUpdateManager: AppUpdateManager,
    private val dispatchers: CoroutineDispatchers,
) : GetAppUpdateInfoInteractor {
    @Suppress("TooGenericExceptionCaught", "MagicNumber", "ComplexMethod")
    override suspend operator fun invoke(): Result = withContext(dispatchers.io) {
        try {
            val appUpdateInfo: AppUpdateInfo = appUpdateManager.requestAppUpdateInfo()
            val priority = appUpdateInfo.updatePriority()
            when (appUpdateInfo.updateAvailability()) {
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> Result.DeveloperTriggeredUpdateInProgress(appUpdateInfo)
                UpdateAvailability.UPDATE_AVAILABLE -> when {
                    /*
                     * To determine priority, Google Play uses an integer value between 0 and 5, with 0 being the default and 5 being the highest
                     * priority. To set the priority for an update, use the inAppUpdatePriority field under Edits.tracks.releases in the
                     * Google Play Developer API. All newly-added versions in the release are considered to be the same priority as the release.
                     * Priority can only be set when rolling out a new release and cannot be changed later.
                     */
                    priority in 0..1 && appUpdateInfo.isFlexibleUpdateAllowed -> Result.LowPriorityUpdateAvailable(appUpdateInfo)
                    priority in 2..3 && appUpdateInfo.isFlexibleUpdateAllowed -> Result.FlexibleUpdateAvailable(appUpdateInfo)
                    priority in 4..5 && appUpdateInfo.isImmediateUpdateAllowed -> Result.ImmediateUpdateAvailable(appUpdateInfo)
                    else -> error("Priority must be between 0 and 5 inclusive: $priority")
                }

                else -> Result.UpdateNotAvailable
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.UpdateNotAvailable
        }
    }
}
