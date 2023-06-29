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

package com.leinardi.forlago.feature.account.interactor.account

import android.accounts.Account
import android.accounts.AccountManager
import com.github.michaelbull.result.mapError
import com.leinardi.forlago.feature.account.AccountAuthenticatorConfig
import com.leinardi.forlago.feature.account.api.interactor.account.AddAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.encryption.EncryptInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class AddAccountInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val dispatchers: CoroutineDispatchers,
    private val encryptInteractor: EncryptInteractor,
    private val getAccessTokenInteractor: GetAccessTokenInteractor,
) : AddAccountInteractor {
    override suspend operator fun invoke(name: String, refreshToken: String): Boolean = withContext(dispatchers.io) {
        val accountCreated = accountManager.addAccountExplicitly(
            Account(name, AccountAuthenticatorConfig.ACCOUNT_TYPE),
            encryptInteractor(refreshToken),
            null,
        )
        getAccessTokenInteractor().mapError { Timber.e("Unable to get accessToken: $it") }
        accountCreated
    }
}
