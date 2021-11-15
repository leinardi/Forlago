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

package com.leinardi.template.core.account.interactor

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import com.leinardi.template.core.account.AccountAuthenticatorConfig
import com.leinardi.template.core.android.coroutine.CoroutineDispatchers
import com.leinardi.template.core.encryption.interactor.DecryptDeterministicallyInteractor
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class GetAccessTokenInteractor @Inject constructor(
    private val accountManager: AccountManager,
    private val decryptDeterministicallyInteractor: DecryptDeterministicallyInteractor,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
) {
    suspend operator fun invoke(): Result {
        val account = getAccountInteractor()
        if (account == null) {
            return Result.Failure.AccountNotFound
        } else {
            val bundle: Bundle = withContext(dispatchers.io) {
                suspendCoroutine { cont ->
                    accountManager.getAuthToken(
                        account,
                        AccountAuthenticatorConfig.AUTHTOKEN_TYPE,
                        null,
                        false,
                        { cont.resumeWith(kotlin.Result.success(it.result)) },
                        null,
                    )
                }
            }

            val accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)?.let { decryptDeterministicallyInteractor(it) }
            if (!accessToken.isNullOrEmpty()) {
                return Result.Success(accessToken)
            }

            val intent = bundle.get(AccountManager.KEY_INTENT) as? Intent
            if (intent != null) {
                return Result.Failure.AuthenticationRequired(intent)
            }

            val message = bundle.getString(AccountManager.KEY_ERROR_MESSAGE)
            when (bundle.getInt(AccountManager.KEY_ERROR_CODE)) {
                AccountManager.ERROR_CODE_BAD_ARGUMENTS -> Result.Failure.BadArguments(message)
                AccountManager.ERROR_CODE_BAD_REQUEST -> Result.Failure.BadRequest(message)
                AccountManager.ERROR_CODE_NETWORK_ERROR -> Result.Failure.NetworkError(message)
            }
            throw IllegalStateException("Unknown bundle value: $bundle")
        }
    }

    sealed class Result {
        data class Success(val accessToken: String) : Result()
        sealed class Failure : Result() {
            object AccountNotFound : Failure()
            data class AuthenticationRequired(val intent: Intent) : Failure()
            data class BadArguments(val errorMessage: String?) : Failure()
            data class BadRequest(val errorMessage: String?) : Failure()
            data class NetworkError(val errorMessage: String?) : Failure()
        }
    }
}
