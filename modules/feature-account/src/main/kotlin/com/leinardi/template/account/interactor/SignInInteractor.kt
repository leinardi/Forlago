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

import kotlinx.coroutines.delay
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class SignInInteractor @Inject constructor() {

    @Suppress("MagicNumber") // We are faking the sign in with the back-end
    suspend operator fun invoke(username: String, password: String): Result {
        // This simulates fetching a new refresh token. The access token won't be valid 20% of the time.
        delay(TimeUnit.SECONDS.toMillis(2))
        return if (Random.nextInt(5) != 0) {
            Result.Success(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis((username.length + password.length).toLong())
            )
        } else {
            Result.Failure.BadAuthentication
        }
    }

    sealed class Result {
        data class Success(
            val accessToken: String,
            val refreshToken: String,
            val expireAtInMillis: Long,
        ) : Result()

        sealed class Failure : Result() {
            object BadAuthentication : Failure()
            object NetworkError : Failure()
        }
    }
}
