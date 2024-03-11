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

package com.leinardi.forlago.library.network.api

import com.apollographql.apollo3.cache.normalized.FetchPolicy.CacheFirst
import com.apollographql.apollo3.cache.normalized.FetchPolicy.CacheOnly
import com.apollographql.apollo3.cache.normalized.FetchPolicy.NetworkFirst
import com.apollographql.apollo3.cache.normalized.FetchPolicy.NetworkOnly

enum class FetchPolicy(val apollo: com.apollographql.apollo3.cache.normalized.FetchPolicy) {
    /**
     * Try the cache, if that failed, try the network.
     *
     * An [ApolloCompositeException] is thrown if the data is not in the cache and the network call failed, otherwise 1 value is emitted.
     *
     * This is the default behaviour.
     */
    CACHE_FIRST(CacheFirst),

    /**
     * Only try the cache.
     *
     * A [CacheMissException] is thrown if the data is not in the cache, otherwise 1 value is emitted.
     */
    CACHE_ONLY(CacheOnly),

    /**
     * Try the network, if that failed, try the cache.
     *
     * An [ApolloCompositeException] is thrown if the network call failed and the data is not in the cache, otherwise 1 value is emitted.
     */
    NETWORK_FIRST(NetworkFirst),

    /**
     * Only try the network.
     *
     * An [ApolloException] is thrown if the network call failed, otherwise 1 value is emitted.
     */
    NETWORK_ONLY(NetworkOnly),
}
