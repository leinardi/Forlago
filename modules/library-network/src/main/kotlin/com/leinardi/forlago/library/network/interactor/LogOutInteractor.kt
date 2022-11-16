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

package com.leinardi.forlago.library.network.interactor

import com.leinardi.forlago.library.android.di.User
import com.leinardi.forlago.library.navigation.api.destination.account.SignInDestination
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.library.preferences.repository.DataStoreRepository
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
            forlagoNavigator.navigate(SignInDestination.get()) {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
