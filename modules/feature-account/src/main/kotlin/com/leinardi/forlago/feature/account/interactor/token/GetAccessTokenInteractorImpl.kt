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

package com.leinardi.forlago.feature.account.interactor.token

import android.accounts.AbstractAccountAuthenticator
import android.accounts.AccountManager
import android.accounts.AuthenticatorException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.leinardi.forlago.feature.account.AccountAuthenticatorConfig
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.encryption.DecryptDeterministicallyInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

internal class GetAccessTokenInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val decryptDeterministicallyInteractor: DecryptDeterministicallyInteractor,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
) : GetAccessTokenInteractor {
    @Suppress("TooGenericExceptionCaught", "Deprecation")
    override suspend operator fun invoke(): GetAccessTokenInteractor.Result = withContext(dispatchers.io) {
        val account = getAccountInteractor()
        if (account == null) {
            return@withContext GetAccessTokenInteractor.Result.Failure.AccountNotFound
        } else {
            try {
                val bundle: Bundle = suspendCoroutine { cont ->
                    accountManager.getAuthToken(
                        account,
                        AccountAuthenticatorConfig.AUTH_TOKEN_TYPE,
                        null,
                        false,
                        {
                            try {
                                cont.resumeWith(kotlin.Result.success(it.result))
                            } catch (e: Exception) {
                                cont.resumeWith(kotlin.Result.failure(e))
                            }
                        },
                        null,
                    )
                }

                val accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)?.let { decryptDeterministicallyInteractor(it) }
                if (!accessToken.isNullOrEmpty()) {
                    return@withContext GetAccessTokenInteractor.Result.Success(accessToken)
                } else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                    Timber.w("Unable to decrypt the access token. Was the app data wiped? Re-authentication required.")
                    accountManager.invalidateAuthToken(account.type, AccountAuthenticatorConfig.AUTH_TOKEN_TYPE)
                    accountManager.setUserData(account, AbstractAccountAuthenticator.KEY_CUSTOM_TOKEN_EXPIRY, 0.toString())
                    invalidateRefreshTokenInteractor()
                    return@withContext GetAccessTokenInteractor.Result.Failure.ReAuthenticationRequired
                }

                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(AccountManager.KEY_INTENT, Intent::class.java)
                } else {
                    bundle[AccountManager.KEY_INTENT] as? Intent
                }
                if (intent != null) {
                    return@withContext GetAccessTokenInteractor.Result.Failure.ReAuthenticationRequired
                }
                error("Unknown bundle value: $bundle")
            } catch (e: IOException) {
                return@withContext GetAccessTokenInteractor.Result.Failure.NetworkError(e.message)
            } catch (e: IllegalArgumentException) {
                return@withContext GetAccessTokenInteractor.Result.Failure.BadArgumentsError(e.message)
            } catch (e: AuthenticatorException) {
                return@withContext GetAccessTokenInteractor.Result.Failure.AccountAuthenticatorError(e.message)
            }
        }
    }
}
