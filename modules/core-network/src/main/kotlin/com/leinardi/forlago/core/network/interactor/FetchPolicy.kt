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

package com.leinardi.forlago.core.network.interactor

import com.apollographql.apollo3.cache.normalized.FetchPolicy.CacheAndNetwork
import com.apollographql.apollo3.cache.normalized.FetchPolicy.CacheFirst
import com.apollographql.apollo3.cache.normalized.FetchPolicy.CacheOnly
import com.apollographql.apollo3.cache.normalized.FetchPolicy.NetworkFirst
import com.apollographql.apollo3.cache.normalized.FetchPolicy.NetworkOnly

enum class FetchPolicy(val apollo: com.apollographql.apollo3.cache.normalized.FetchPolicy) {
    /**
     * Try the cache, then also try the network.
     *
     * If the data is in the cache, it is emitted, if not, no exception is thrown at that point. Then the network call is made, and if
     * successful, the value is emitted, if not, either an [ApolloCompositeException] (both cache miss and network failed) or an
     * [ApolloException] (only network failed) is thrown.
     */
    CACHE_AND_NETWORK(CacheAndNetwork),

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
