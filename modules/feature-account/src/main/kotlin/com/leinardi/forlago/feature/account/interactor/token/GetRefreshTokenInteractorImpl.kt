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

package com.leinardi.forlago.feature.account.interactor.token

import android.accounts.AccountManager
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.GetRefreshTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.encryption.DecryptInteractor
import com.leinardi.forlago.library.annotation.AutoBind
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.security.GeneralSecurityException
import javax.inject.Inject

@AutoBind
internal class GetRefreshTokenInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val decryptInteractor: DecryptInteractor,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
) : GetRefreshTokenInteractor {
    override suspend operator fun invoke(): String? = withContext(dispatchers.io) {
        getAccountInteractor()?.let { account ->
            accountManager.getPassword(account)?.let { refreshToken ->
                try {
                    decryptInteractor(refreshToken)
                } catch (e: GeneralSecurityException) {
                    Timber.e(e, "Unable to decrypt the refreshToken. Invalidating it to trigger a new authentication")
                    invalidateRefreshTokenInteractor()
                    null
                }
            }
        }
    }
}
