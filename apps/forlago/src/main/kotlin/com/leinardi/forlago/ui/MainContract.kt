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

package com.leinardi.forlago.ui

import android.content.Intent
import androidx.annotation.StringRes
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.AppUpdateType
import com.leinardi.forlago.library.ui.base.ViewEffect
import com.leinardi.forlago.library.ui.base.ViewEvent
import com.leinardi.forlago.library.ui.base.ViewState

object MainContract {
    data class State(
        val startDestination: String,
        val dynamicColors: Boolean = false,
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnIntentReceived(val intent: Intent, val isNewIntent: Boolean = false) : Event()
        object OnInAppUpdateCancelled : Event()
        object OnInAppUpdateFailed : Event()
        object OnShown : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorSnackbar(
            val message: String,
            @StringRes val actionLabel: Int = com.leinardi.forlago.library.i18n.R.string.i18n_generic_snackbar_dismissal,
        ) : Effect()

        data class StartUpdateFlowForResult(val appUpdateInfo: AppUpdateInfo, @AppUpdateType val appUpdateType: Int) : Effect()
        object FinishActivity : Effect()
        object ShowSnackbarForCompleteUpdate : Effect()
    }
}
