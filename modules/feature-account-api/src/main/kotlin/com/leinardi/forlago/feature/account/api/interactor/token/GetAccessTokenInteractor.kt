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

package com.leinardi.forlago.feature.account.api.interactor.token

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.leinardi.forlago.library.network.api.model.AuthCallError

interface GetAccessTokenInteractor {
    suspend operator fun invoke(): Result<String, ErrResult>

    sealed class ErrResult {
            data class AccountAuthenticatorError(val errorMessage: String?) : ErrResult()
            data class BadArgumentsError(val errorMessage: String?) : ErrResult()
            data class NetworkError(val errorMessage: String?) : ErrResult()
            object AccountNotFound : ErrResult()
            object ReAuthenticationRequired : ErrResult()
    }
}

fun Result<String, GetAccessTokenInteractor.ErrResult>.mapToAuthCallError(): Result<String, AuthCallError> = mapError { errResult ->
    when (errResult) {
        is GetAccessTokenInteractor.ErrResult.AccountAuthenticatorError -> AuthCallError.UnrecoverableError(errResult.errorMessage)
        is GetAccessTokenInteractor.ErrResult.AccountNotFound -> AuthCallError.UnrecoverableError()
        is GetAccessTokenInteractor.ErrResult.BadArgumentsError -> AuthCallError.UnrecoverableError(errResult.errorMessage)
        is GetAccessTokenInteractor.ErrResult.NetworkError -> AuthCallError.NetworkError(errResult.errorMessage)
        is GetAccessTokenInteractor.ErrResult.ReAuthenticationRequired -> AuthCallError.ReAuthenticationRequired
    }
}
