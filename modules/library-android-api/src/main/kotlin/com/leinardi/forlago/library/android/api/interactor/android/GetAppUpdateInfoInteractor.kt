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

package com.leinardi.forlago.library.android.api.interactor.android

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.AppUpdateType

interface GetAppUpdateInfoInteractor {
    suspend operator fun invoke(): Result

    sealed class Result {
        data class LowPriorityUpdateAvailable(
            val appUpdateInfo: AppUpdateInfo,
            @AppUpdateType val appUpdateType: Int = AppUpdateType.FLEXIBLE,
        ) : Result()

        data class FlexibleUpdateAvailable(
            val appUpdateInfo: AppUpdateInfo,
            @AppUpdateType val appUpdateType: Int = AppUpdateType.FLEXIBLE,
        ) : Result()

        data class ImmediateUpdateAvailable(
            val appUpdateInfo: AppUpdateInfo,
            @AppUpdateType val appUpdateType: Int = AppUpdateType.IMMEDIATE,
        ) : Result()

        data class DeveloperTriggeredUpdateInProgress(
            val appUpdateInfo: AppUpdateInfo,
            @AppUpdateType val appUpdateType: Int = AppUpdateType.IMMEDIATE,
        ) : Result()

        object UpdateNotAvailable : Result()
    }
}
