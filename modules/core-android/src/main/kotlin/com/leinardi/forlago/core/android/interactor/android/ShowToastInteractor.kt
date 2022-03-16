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

import android.app.Application
import android.widget.Toast
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowToastInteractor @Inject constructor(
    private val application: Application,
) {
    private var toast: Toast? = null

    operator fun invoke(text: String, shortLength: Boolean = false, cancelPrevious: Boolean = true) {
        if (cancelPrevious) {
            toast?.run { cancel() }
        }
        toast = Toast.makeText(application, text, if (shortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).also { it.show() }
    }
}
