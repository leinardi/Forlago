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

package com.leinardi.forlago.library.logging.interactor

import com.leinardi.forlago.library.annotation.AutoBind
import com.leinardi.forlago.library.logging.BuildConfig
import com.leinardi.forlago.library.logging.api.interactor.LogScreenViewInteractor
import timber.log.Timber
import javax.inject.Inject

@AutoBind
class LogScreenViewInteractorImpl @Inject constructor(
// private val firebaseAnalytics: FirebaseAnalytics,
) : LogScreenViewInteractor {
    override operator fun invoke(screenClass: String, screenName: String) {
        if (!BuildConfig.DEBUG) {
            Timber.e("Default FirebaseApp must be initialized for the log to work")
            // firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            // param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            // param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            // }
        }
    }
}
