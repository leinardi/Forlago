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

package com.leinardi.forlago.feature.account.interactor.account

import com.leinardi.forlago.feature.account.api.interactor.account.LogOutInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.destination.account.SignInDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import com.leinardi.forlago.library.preferences.api.di.User
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import timber.log.Timber
import javax.inject.Inject

internal class LogOutInteractorImpl @Inject constructor(
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor,
    private val forlagoNavigator: ForlagoNavigator,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    @User private val userDataStoreRepository: DataStoreRepository,
) : LogOutInteractor {
    private var signOutInProgress: Boolean = false

    override suspend operator fun invoke(navigateToLogin: Boolean) {
        Timber.d("LogOut")
        signOutInProgress = true
        clearApolloCacheInteractor()
        getFeaturesInteractor().forEach { it.featureLifecycle.onSignOut }
        userDataStoreRepository.clearPreferencesStorage()
        if (navigateToLogin) {
            forlagoNavigator.navigate(SignInDestination.get()) {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
        }
        signOutInProgress = false
    }

    override fun isSignOutInProgress() = signOutInProgress
}
