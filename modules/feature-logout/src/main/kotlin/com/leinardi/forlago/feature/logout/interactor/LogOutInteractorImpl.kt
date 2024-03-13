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

package com.leinardi.forlago.feature.logout.interactor

import com.leinardi.forlago.feature.login.api.interactor.IsLogInInProgressInteractor
import com.leinardi.forlago.feature.logout.api.interactor.LogOutInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import dagger.hilt.android.scopes.ActivityRetainedScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
internal class LogOutInteractorImpl @Inject constructor(
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val isLogInInProgressInteractor: IsLogInInProgressInteractor,
) : LogOutInteractor {
    private var logOutInProgress: Boolean = false

    override suspend operator fun invoke(navigateToLogin: Boolean) {
        if (!isLogInInProgressInteractor()) {
            Timber.d("LogOut")
            logOutInProgress = true
            clearApolloCacheInteractor()
            getFeaturesInteractor().forEach { it.featureLifecycle.onLogout() }
            if (navigateToLogin) {
                forlagoNavigator.navigateToLogin {
                    launchSingleTop = true
                    popUpTo(0) { inclusive = true }
                }
            }
            logOutInProgress = false
        } else {
            Timber.w("LogIn in progress, ignoring LogOut request!")
        }
    }

    override fun isLogOutInProgress() = logOutInProgress
}
