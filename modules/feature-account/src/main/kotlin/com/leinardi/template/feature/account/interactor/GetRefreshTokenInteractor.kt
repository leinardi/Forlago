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
import com.leinardi.template.core.account.interactor.GetAccountInteractor
import com.leinardi.template.core.android.coroutine.CoroutineDispatchers
import com.leinardi.template.core.encryption.interactor.DecryptInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.security.GeneralSecurityException
import javax.inject.Inject

class GetRefreshTokenInteractor @Inject constructor(
    private val accountManager: AccountManager,
    private val decryptInteractor: DecryptInteractor,
    private val dispatchers: CoroutineDispatchers,
    private val getAccountInteractor: GetAccountInteractor,
    private val invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
) {
    suspend operator fun invoke(): String? = withContext(dispatchers.io) {
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
