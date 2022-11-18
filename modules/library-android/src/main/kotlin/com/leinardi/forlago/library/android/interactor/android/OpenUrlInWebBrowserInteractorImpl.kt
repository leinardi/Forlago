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

package com.leinardi.forlago.library.android.interactor.android

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.leinardi.forlago.library.android.R
import com.leinardi.forlago.library.android.api.interactor.android.OpenUrlInWebBrowserInteractor
import com.leinardi.forlago.library.android.api.interactor.android.ShowToastInteractor
import timber.log.Timber
import javax.inject.Inject

internal class OpenUrlInWebBrowserInteractorImpl @Inject constructor(
    private val application: Application,
    private val showToastInteractor: ShowToastInteractor,
) : OpenUrlInWebBrowserInteractor {
    override operator fun invoke(url: String) {
        try {
            application.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (exception: ActivityNotFoundException) {
            Timber.i(exception)
            showToastInteractor(application.getString(R.string.i18n_generic_error_no_browser_installed))
        }
    }
}
