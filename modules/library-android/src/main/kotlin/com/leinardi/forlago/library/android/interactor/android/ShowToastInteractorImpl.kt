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

package com.leinardi.forlago.library.android.interactor.android

import android.app.Application
import android.widget.Toast
import com.leinardi.forlago.library.android.api.interactor.android.ShowToastInteractor
import com.leinardi.forlago.library.android.api.strictmode.noStrictMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShowToastInteractorImpl @Inject constructor(
    private val application: Application,
) : ShowToastInteractor {
    private var toast: Toast? = null

    override operator fun invoke(text: String, shortLength: Boolean, cancelPrevious: Boolean) {
        if (cancelPrevious) {
            toast?.run { cancel() }
        }
        // StrictMode complains about wrong context used to show the toast, but it's a false positive since the official documentation states that
        // an application context can and should be used: https://developer.android.com/guide/topics/ui/notifiers/toasts#Basics
        toast = noStrictMode(disableThread = false) {
            Toast.makeText(application, text, if (shortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).apply { show() }
        }
    }
}
