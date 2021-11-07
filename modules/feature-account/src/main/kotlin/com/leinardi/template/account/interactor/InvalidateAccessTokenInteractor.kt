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

import android.accounts.AbstractAccountAuthenticator
import android.accounts.AccountManager
import com.leinardi.template.account.authenticator.AccountAuthenticator
import com.leinardi.template.android.coroutine.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvalidateAccessTokenInteractor @Inject constructor(
    private val accountManager: AccountManager,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
    private val peekAccessTokenInteractor: PeekAccessTokenInteractor,
) {
    suspend operator fun invoke() {
        withContext(dispatchers.io) {
            getAccountInteractor()?.let { account ->
                val accessToken = peekAccessTokenInteractor()
                accountManager.invalidateAuthToken(AccountAuthenticator.ACCOUNT_TYPE, accessToken)
                accountManager.setUserData(account, AbstractAccountAuthenticator.KEY_CUSTOM_TOKEN_EXPIRY, 0.toString())
            }
        }
    }
}
