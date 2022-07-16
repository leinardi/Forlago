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

package com.leinardi.forlago.library.network.interactor

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.apolloStore
import com.leinardi.forlago.library.android.coroutine.CoroutineDispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ClearApolloCacheInteractor @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dispatchers: CoroutineDispatchers,
) {
    suspend operator fun invoke() = withContext(dispatchers.io) {
        Timber.i("Clearing Apollo Store Cache")
        apolloClient.apolloStore.clearAll()
    }
}
