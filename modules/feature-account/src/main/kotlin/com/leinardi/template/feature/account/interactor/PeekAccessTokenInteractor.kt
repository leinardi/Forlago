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

package com.leinardi.template.feature.account.interactor

import android.accounts.AccountManager
import com.leinardi.template.core.account.AccountAuthenticatorConfig
import com.leinardi.template.core.account.interactor.GetAccountInteractor
import com.leinardi.template.core.android.coroutine.CoroutineDispatchers
import com.leinardi.template.core.encryption.interactor.DecryptDeterministicallyInteractor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PeekAccessTokenInteractor @Inject constructor(
    private val accountManager: AccountManager,
    private val decryptDeterministicallyInteractor: DecryptDeterministicallyInteractor,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
) {
    suspend operator fun invoke(): String? = withContext(dispatchers.io) {
        getAccountInteractor()?.let { account ->
            accountManager.peekAuthToken(account, AccountAuthenticatorConfig.AUTHTOKEN_TYPE)?.let { decryptDeterministicallyInteractor(it) }
        }
    }
}
