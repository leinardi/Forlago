/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.account.interactor

import android.accounts.Account
import android.accounts.AccountManager
import com.leinardi.template.account.authenticator.AccountAuthenticator
import com.leinardi.template.android.coroutine.CoroutineDispatchers
import com.leinardi.template.encryption.interactor.EncryptInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AddAccountInteractor @Inject constructor(
    private val accountManager: AccountManager,
    private val dispatchers: CoroutineDispatchers,
    private val encryptInteractor: EncryptInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
) {
    suspend operator fun invoke(name: String, refreshToken: String): Boolean = withContext(dispatchers.io) {
        val accountCreated = accountManager.addAccountExplicitly(
            Account(name, AccountAuthenticator.ACCOUNT_TYPE),
            encryptInteractor(refreshToken),
            null
        )
        val result = getAccessTokenInteractor()
        if (result is GetAccessTokenInteractor.Result.Failure) {
            Timber.e("Unable to get accessToken: $result")
        }
        accountCreated
    }
}
