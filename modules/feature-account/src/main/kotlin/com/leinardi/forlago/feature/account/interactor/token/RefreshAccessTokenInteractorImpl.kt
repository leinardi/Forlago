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

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.leinardi.forlago.feature.account.api.interactor.token.RefreshAccessTokenInteractor
import com.leinardi.forlago.library.network.api.model.AuthErrResult
import kotlinx.coroutines.delay
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class RefreshAccessTokenInteractorImpl @Inject constructor() : RefreshAccessTokenInteractor {
    override suspend operator fun invoke(refreshToken: String): Result<String, AuthErrResult> {
        delay(TimeUnit.SECONDS.toMillis(1))
        return Ok(UUID.randomUUID().toString())
    }
}
