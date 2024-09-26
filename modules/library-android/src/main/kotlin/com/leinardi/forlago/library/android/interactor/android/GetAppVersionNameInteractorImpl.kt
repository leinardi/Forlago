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

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.leinardi.forlago.library.android.api.interactor.android.GetAppVersionNameInteractor
import com.leinardi.forlago.library.annotation.AutoBind
import timber.log.Timber
import javax.inject.Inject

@AutoBind
internal class GetAppVersionNameInteractorImpl @Inject constructor(
    private val application: Application,
) : GetAppVersionNameInteractor {
    @Suppress("DEPRECATION")
    override operator fun invoke(): String? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            application.packageManager.getPackageInfo(application.packageName, PackageManager.PackageInfoFlags.of(0)).versionName
        } else {
            application.packageManager.getPackageInfo(application.packageName, 0).versionName
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}
