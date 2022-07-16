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

package com.leinardi.forlago.library.network.interactor.account

import kotlinx.coroutines.delay
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RefreshAccessTokenInteractor @Inject constructor() {
    suspend operator fun invoke(refreshToken: String): Result {
        delay(TimeUnit.SECONDS.toMillis(1))
        return Result.Success(
            UUID.randomUUID().toString(),
            System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(refreshToken.length.toLong()),
        )
    }

    sealed class Result {
        data class Success(
            val accessToken: String,
            val expiryInMillis: Long,
        ) : Result()

        sealed class Failure : Result() {
            data class BadAuthentication(val code: Int) : Failure()
            data class NetworkError(val throwable: Throwable) : Failure()
            data class UnexpectedError(val throwable: Throwable, val code: Int? = null) : Failure()
        }
    }
}
