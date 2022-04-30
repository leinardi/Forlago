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

package com.leinardi.forlago.core.network.interactor

import com.leinardi.forlago.core.android.di.User
import com.leinardi.forlago.core.navigation.ForlagoNavigator
import com.leinardi.forlago.core.navigation.destination.account.SignInDestination
import com.leinardi.forlago.core.network.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.core.preferences.repository.DataStoreRepository
import timber.log.Timber
import javax.inject.Inject

class LogOutInteractor @Inject constructor(
    private val forlagoNavigator: ForlagoNavigator,
    private val removeAccountsInteractor: RemoveAccountsInteractor,
    @User private val userDataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(navigateToLogin: Boolean = true) {
        Timber.d("LogOut")
        removeAccountsInteractor()
        userDataStoreRepository.clearPreferencesStorage()
        if (navigateToLogin) {
            forlagoNavigator.navigate(SignInDestination.createRoute()) {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
